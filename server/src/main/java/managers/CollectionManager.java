package managers;

import interfaces.Collectionable;
import models.*;

import java.time.LocalDateTime;
import java.util.*;

public class CollectionManager implements Collectionable<Dragon> {
    private LocalDateTime time = LocalDateTime.now();
    public static int user_id;
    public static boolean authoriz;
    public static boolean pass;
    private static Integer notId = 0;
    public static LinkedList<Dragon> dragons = new LinkedList<>();
    private boolean flag=true;
    public static ArrayList<String> filesName= new ArrayList<String>();
    Random random = new Random();
    public static LinkedList<Users> users =new LinkedList<>();
    private Dragon dragon_1=null;
    public static int cycle=0;
    private int anInt =1;
    public static Integer user_id_max=0;
    private String[] path=new String[100];
    public static Integer dragon_id_max;
    private WorkDataBase worker =new WorkDataBase();
    static{
        LocalDateTime localdatetime = LocalDateTime.now();
    }
    private static CollectionManager collection;

    @Override
    public void setCollection(LinkedList<Dragon> dragons) {
        this.dragons = dragons;
    }

    @Override
    public LinkedList<Dragon> getCollection() {
        return dragons;
    }

    private CollectionManager() {
    }
    public String authorization(String user, String password){
        for (Users user1: users){
            if (user1.getName().equals( user)){
                if (user1.getPassword().equals(password)){
                    authoriz = true;
                    user_id = user1.getId();
                    return "Приветствуем вас, спасибо,что выбираете нас";
                }
            }
        }
        authoriz=false;
        return "Пароль или никнейм был введен неверно";
    }
    public String regIn(String user, String password){
        for (Users user1: users){
            if ((user1.getName()).equals(user)){
                authoriz=false;
                user_id=1;
                return "Данное имя пользователя занято";
            }
        }
        String salt = PasswordUtils.generateSalt();
        worker.addUser(user,password,salt);
        authoriz=true;
        user_id=1;
        user_id_max+=1;
        users.add(new Users(user, password,user_id_max,salt));
        return "Пользователь добавлен";
    }
    @Override
    public String printInfo() {
        return ("Тип коллекции: " + dragons.getClass().getName() + "\nДата инициализации: " + time + "\nКоличество элементов: " + dragons.size());
    }

    @Override
    public String print() {
        if (dragons.size() == 0) {
            return ("Коллекция пуста");
        }
        String result = "";
        for (Dragon dragon : dragons) {
            result += ("Дракон " + dragon.getId() + " {\n") +
            (dragon.toString()) + "\n" +
            ("}");
        }
        return result;
    }

    @Override
    public String clear() {
        dragons.clear();
        return "Коллекция очищена успешно!\n";

    }
    public String exit(){
        Server.logger.info("Сервер был отключен");
        System.exit(0);
        return "Выход из программы";
    }
    public String save(){
        return "Сохранено";
    }
    public String max_by_weight() {
        Dragon max_weight_dragon = collection.getCollection().getFirst();
        Long max_weight = collection.getCollection().getFirst().getWeight();
        for (Dragon dragon : collection.getData().getCollection()) {
            if (dragon.getWeight() > max_weight) {
                max_weight_dragon = dragon;
                max_weight = dragon.getWeight();
            }
        }
        return "Максимальный по весу дракон: " + (max_weight_dragon.toString());
    }

    public String head() {
        if (collection.getCollection() != null) {
            return "1-ый элемент коллекции {" + "\n" +
            collection.getCollection().getFirst().toString() +
            "}";
        } else {
            return ("Коллекция пуста");
        }
    }

    public String group_by() {
        String finalresult = "";
        for (DragonType cnt : DragonType.values()) {
            Integer value = 0;
            String result = "";
            for (Dragon dragon : collection.getCollection()) {
                if (dragon.getType() == cnt) {
                    result += (dragon.toString()) + "\n";
                    value++;
                }
            }
            finalresult += "Вывод драконов типа " + cnt.toString() + " {\n" + result + "}\nДраконов типа " + cnt.toString() + ": " + value + "\n";
        }
        return finalresult;
    }

    public String filter(String data) {
        boolean found = false;
        try {
            String result = "";
            Integer value = Integer.parseInt(data);
            for (Dragon dragon : collection.getCollection()) {
                if (dragon.getCharacter() != null && dragon.getCharacter().ordinal() < value) {
                    result += (dragon.toString()) + "\n";
                    found = true;
                }
            }
            if (!found) {
                return("Дракона с меньшим значением характера не найдено");
            }
            return result;
        } catch (NumberFormatException e) {return ("Параметр команды должен быть числом");
        }
    }
    public void sortManager(){
        Collections.sort(dragons, new Comparator<Dragon>() {
            @Override
            public int compare(Dragon o1, Dragon o2) {
                return o1.getId().compareTo(o2.getId());            }
        });
    }
    public String remove_head(Integer id) {
        try{

            sortManager();
            if (Objects.equals(dragons.get(0).getUser_id(), id)) {
                String str = dragons.get(0).toString();
                worker.deleteDragonById(dragons.get(0).getId());
                dragons.remove(0);
                return "Первый элемент(delete): \n" + str;
            }else{
                return "Данный элемент принадлежит не вам. В удалении отказано";
            }
        }catch (java.lang.IndexOutOfBoundsException E){
            return "Коллекция драконов пустая. Первый элемент удален не был";
        }
    }

    public String remove_by_id(String arg, Integer user_id) {
        int index = 0;
        String res = "";
        long id_1 = -1;
        try {
            id_1 = Long.parseLong(arg);
        } catch (NumberFormatException e) {
            Server.logger.info("Клиент отправил некорректный id " + id_1);
        }


        long finalId_ = id_1;
        for (Dragon city : dragons) {
            if (city.getId() == id_1) {
                if (Objects.equals(city.getUser_id(), user_id)) {
                    res += city.toString();
                    worker.deleteDragonById(id_1);
                    dragons.remove(city);
                    break;
                } else {
                    return "У вас недостаточно прав на удаление данного объекта";
                }


            }

        }
        if (res == "") {
            return "Городов с данным id не было найдено";
        } else {
            return "Были удален: " + res;
        }
    }

    @Override
    public Dragon search(int id) {
        for (Dragon dragon : dragons) {
            if (id == dragon.getId()) {
                return dragon;
            }
        }
        System.out.println("Объекта с таким id не найдено в коллекции");
        return null;
    }

    @Override
    public void remove(Dragon dragon) {
        dragons.remove(dragon);
    }

    public static CollectionManager getData() {
        if (collection == null) {
            collection = new CollectionManager();
        }
        return collection;
    }

    public String update_id(Dragon dragon, Integer user_id) throws NumberFormatException {
        int count=0;
        int m=0;
        long id_1=dragon.getId();

        for (Dragon dragon_it:dragons){
            if (dragon_it.getId() ==id_1 && Objects.equals(dragon_1.getUser_id(), user_id)){
                m=-1;
            }

        }

        if (m==-1) {
            for (Dragon dragon_it : dragons) {
                if (id_1 == dragon_it.getId()) {
                    notId=1;
                    worker.updateDragonById(dragon_1);
                    dragons.remove(dragon_it);
                    dragons.add(dragon);
                    return ("Данные введены корректно, коллекция обновлена");

                }
            }
        }else{
            return ("Города под данным id не найдено");

        }
        return "";
    }




    public String add(Dragon dragon, Integer id){

        worker.addDragon(dragon);

        dragon_id_max+=1;
        dragon.setId(dragon_id_max);
        dragons.add(dragon);
        return ("Данные введены корректно, коллекция добавлена");
    }
    @Override
    public String put(Dragon dragon){
        worker.addDragon(dragon);
        return "Dragon " + dragon.getName() + " добавлен в коллекцию";
    }
}
