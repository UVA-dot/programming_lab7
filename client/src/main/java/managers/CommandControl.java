package managers;
import managers.commands.Command;
import request.CommandRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class CommandControl {
    public static int work=0;
    public String path ="";
    public static int count =0;
    public static int mode =0;
    public static String inputData="";
    public static int length;
    private Scanner scanner =new Scanner(System.in);
    public static int is=-1;
    private static Map<String, Command> comands = new HashMap<>();
    public void addCommand(String name,Command command){
        comands.put(name,command);
    }
    public Command getCommand(String name){
        return comands.get(name);
    }

    public String reader(){
        Command command;
        String println="";
        if (mode ==0) {
            try {
                println = scanner.nextLine();
            }catch (NoSuchElementException e){
                System.exit(0);
            }


            return println;
            // Обработка данных из строки
        } else {
            // Обработка данных из файла по одной строке
            try{
                File file =new File(CollectionManager.listPath.get(is).getPath());
                Scanner scan = new Scanner(file);//
                for (int i=0;i<=CollectionManager.listPath.get(is).getCount() && i<CollectionManager.listPath.get(is).getLength();i+=1) {
                    println = scan.nextLine().trim();
                }
                if (CollectionManager.listPath.get(is).getCount()<CollectionManager.listPath.get(is).getLength()){
                    CollectionManager.listPath.get(is).setCount(CollectionManager.listPath.get(is).getCount()+1);
                    if (work==0){
                        // Разделяем строку по пробелам
                        String[] argument = println.split("\\s+");
                        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(argument));
                        String[] words=new String[0];
                        String line="";
                        int danger=0;
                        int in=0;
                        try{
                            for(String str: arrayList){
                                if ((str.equals(" ") || str.equals(""))&&(danger==0)){
                                    if (str.equals(" ")){
                                        arrayList.remove(in);
                                    }
                                    if (str.equals("")){
                                        arrayList.remove(in);
                                    }
                                }else {
                                    danger+=1;
                                }
                                in+=1;

                            }
                            words = arrayList.toArray(new String[0]);
                            if (words.length > 1) {
                                // Получаем первое слово
                                println = words[0];
                                String firstWord=words[0];

                                // Удаляем пробелы перед следующими словами
                                StringBuilder result = new StringBuilder("");
                                for (int i = 1; i < words.length; i++) {
                                    result.append(words[i]);
                                    line = line + " " + words[i];
                                }

                                // Выводим результат
                                line=line.trim();
                            }else {
                                println=words[0];
                            }
                        }catch (ConcurrentModificationException У){

                        }
                        println = println.toLowerCase();
                        CollectionManager.command=println;
                        switch (println) {
                            case "info","help","exit","show","clear","head","remove_head","min_by_capital":
                                System.out.println("Выполняется команда "+println);
                                if (words.length==1){
                                    getCommand(println).execute(println);
                                }else {
                                    System.out.println("Данная команда вводится без аргумента");
                                    CollectionManager.request= new CommandRequest("skip");
                                }
                                break;
                            case "count_less_than_climate","filter_starts_with_name","remove_by_id","update":
                                if (words.length==2) {
                                    work=1;
                                    System.out.println("Выполняется команда "+println+" с аргументом " + line);
                                    getCommand(println).execute(line);
                                } else if (words.length==1) {
                                    System.out.println("Данная команда вводится с аргументом");
                                    CollectionManager.request=new CommandRequest("skip");
                                }else{
                                    System.out.println("Данная команда вводится с одним аргументом");
                                    CollectionManager.request=new CommandRequest("skip");
                                }

                                break;
                            case "add","add_if_max":
                                work=1;
                                System.out.println("Выполняется команда "+println);
                                if (words.length==1){
                                    getCommand(println).execute(line);
                                }else {
                                    System.out.println("Данная команда вводится без аргумента");
                                    CollectionManager.request=new CommandRequest("skip");
                                    work=0;
                                }
                                break;
                            case "execute_script":
                                System.out.println("Выполняется команда " + println + " ищет файл " + line);
                                File file2=new File(line);
                                if(file2.exists()){
                                    work=1;
                                    getCommand(println).execute(line);
                                }else {
                                    System.out.println("Скрипта с таким путем не было найдено");
                                    CollectionManager.request=new CommandRequest("skip");
                                }

                                break;
                            default:
                                work=0;
                                System.out.println(new IllegalArgumentException("Неизвестная команда: " + println));
                                CollectionManager.request=new CommandRequest("skip");
                                break;
                        }
                        return "";
                    }else{
                        System.out.println("Из скрипта было получено: "+println);
                        return println;
                    }

                }else{
                    if (is==0){
                        CommandControl.mode=0;

                    }
                    if (is>0){

                        CollectionManager.listPath.remove(CommandControl.is);
                        CollectionManager.filesName.remove(CommandControl.is);
                        is-=1;
                    }
                }
                scan.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}