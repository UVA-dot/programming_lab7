package managers.commands;

import managers.CommandManager;
import models.Dragon;

public class History extends Command{
    public History(){
        super("History", "Выводит последние 10 команд(без аргументов)");
    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        return commandManager.printHistory();
    }
}
