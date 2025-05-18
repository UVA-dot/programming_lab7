package managers;
import managers.CollectionManager;
import managers.ConsoleReaderThread;
import managers.Controller;
import models.Coordinates;
import models.Dragon;
import request.CommandRequest;
import response.CommandResponse;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

public class Server {
    private Selector selector;
    private InetSocketAddress address;
    private Set<SocketChannel> session;
    public static final Logger logger = Logger.getGlobal();
    private ExecutorService processPool;
    private ExecutorService readPool;
    private ExecutorService writePool;
    private Controller controller = new Controller();

    public Server(String host, int port) {
        this.readPool = Executors.newFixedThreadPool(5);
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        this.processPool = Executors.newCachedThreadPool(); // Cached thread pool for processing
        this.writePool = Executors.newFixedThreadPool(5); // ForkJoinPool for writing
    }

    public void start() throws IOException, ClassNotFoundException, java.io.IOException {
        Thread readerThread = new Thread(new ConsoleReaderThread());
        readerThread.start();

        controller.createCommand();
        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        logger.info("Сервер запущен, ожидает клиентов" + address.toString());
        while (true) {
            this.selector.select();
            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();
                keys.remove();
                if (!key.isValid()) continue;
                if (key.isAcceptable()) accept(key);
                if (key.isReadable()) read(key);
            }
        }
    }

    private void accept(SelectionKey key) throws IOException, java.io.IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
        this.session.add(channel);
        logger.info("К серверу подключился новый клиент " + channel.socket().getRemoteSocketAddress());
    }

    private void read(SelectionKey key) {
        readPool.submit(() -> {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
            int numRead;
            try {
                numRead = channel.read(byteBuffer);
                if(numRead == 0){
                    return;
                }
                if (numRead == -1) {
                    throw new SocketException("Client disconnected");
                }
            } catch (IOException e) {
                synchronized (session) {
                    session.remove(channel);
                }
                logger.info("Вышел клиент: " + channel.socket().getRemoteSocketAddress());
                try {
                    channel.close();
                } catch (IOException ex) {
                    // Handle exception
                }
                key.cancel();
                return;
            }

            byteBuffer.flip();
            ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array(), 0, numRead);
            ObjectInputStream ois = null;
            CommandRequest request = null;
            try {
                ois = new ObjectInputStream(bis);
                request = (CommandRequest) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                logger.info(e.getMessage());
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                    bis.close();
                } catch (IOException e) {
                    // Handle exception
                }
            }

            if (request != null) {
                processRequest(channel, request);
            }
        });
    }

    private void processRequest(SocketChannel channel, CommandRequest dragon) {
        processPool.submit(() -> {
            if (dragon.getLine() != null && dragon.getDragon() != null) {
                logger.info("Сервер получил от клиента <" + channel.socket().getRemoteSocketAddress() + "> команду: " + dragon.getName() + ", её аргумент: " + dragon.getLine() + ", а также дракона: " + dragon.getDragon().toString());


            } else if (dragon.getLine() != null) {
                logger.info("Сервер получил от клиента <" + channel.socket().getRemoteSocketAddress() + "> команду: " + dragon.getName() + ", её аргумент: " + dragon.getLine());

            } else if (dragon.getDragon() != null) {
                logger.info("Сервер получил от клиента <" + channel.socket().getRemoteSocketAddress() + "> команду: " + dragon.getName() + ", а также дракона: " + dragon.getDragon().toString());


            } else {
                logger.info("Сервер получил от клиента <" + channel.socket().getRemoteSocketAddress() + "> команду: " + dragon.getName());

            }

            Dragon dragon_new = null;

            try {
                Coordinates coordinates = new Coordinates(dragon.getDragon().getCoordinates().getX(), dragon.getDragon().getCoordinates().getY());
                dragon_new = new Dragon(dragon.getDragon().getId(),
                        dragon.getDragon().getName(),
                        coordinates, LocalDateTime.now(),
                        dragon.getDragon().getAge(),
                        dragon.getDragon().getWeight(),
                        dragon.getDragon().getType(),
                        dragon.getDragon().getCharacter(),
                        dragon.getDragon().getHead());
            } catch (NullPointerException e) {

            }
            Controller controller = new Controller();
            try {
                String result = controller.run(dragon.getName(), dragon_new, dragon.line, dragon.getUser(), dragon.getPassword(), dragon.getId());
                CommandResponse commandsResponse = new CommandResponse(dragon.getName(), null, result);
                sendResponse(channel, commandsResponse);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            ;
        });
    }

    private void sendResponse(SocketChannel channel, CommandResponse response) {
        writePool.submit(() -> {
            try {
                logger.info("Сервер отправил клиенту <" + channel.socket().getRemoteSocketAddress() + "> сообщение: " + response.getResult());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.flush();
                byte[] responseBytes = baos.toByteArray();

                ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
                sizeBuffer.putInt(responseBytes.length);
                sizeBuffer.flip();
                channel.write(sizeBuffer);

                ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
                channel.write(responseBuffer);
            } catch (IOException e) {
                logger.info("Ошибка отправки сообщения клиенту: " + e.getMessage());
            }
        });
    }

}