package managers;
import interfaces.Collectionable;
import interfaces.Console;
import managers.commands.Command;
import models.Dragon;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ConsoleManager implements Console {
    private Scanner scanner;
    private CommandManager commandManager;
    private Validator validator;
    public Integer id = 1;
    public static Integer work = 0;
    private CollectionManager collectionManager = CollectionManager.getData();
    public ConsoleManager(Scanner scanner, CommandManager commandManager, Validator validator){
        this.commandManager = commandManager;
        this.scanner = scanner;
        this.validator = validator;
    }
    public Integer setId(){
        return id + 1;
    }
    public LocalDateTime setTime(){
        return LocalDateTime.now();
    }
    public String inputFieldString(){
        String curField = null;
        if(scanner.hasNextLine()){
            curField = scanner.nextLine().trim();
        }
        return curField;
    }
    public Integer inputFieldNumber(){
        String curField = null;
        try {
            if (scanner.hasNextLine()) {
                curField = scanner.nextLine().trim();
            }
            return Integer.parseInt(curField);
        }
        catch(NumberFormatException e){
            System.out.println("Введено не число");
        }
        return null;
    }
    @Override
    public void executeConsole(){
        String curcmd;
        while(scanner.hasNext()){
            String line = scanner.nextLine().trim();
            if(!line.isEmpty()){
                if(line.equalsIgnoreCase("EXIT")) break;
                else{
                    String[] input = line.split(" ");
                    curcmd = input[0];
                    Command cmd = commandManager.getCommand(curcmd);
                    if(cmd == null){
                        System.out.println("Команда не найдена");
                        commandManager.getCommand("help").execute();
                    }
                    else{
                        commandManager.updateHistory(cmd);
                        String args;
                        args = input[1];
                        cmd.execute(args);

                    }
                }
            }
        }
    }
}
