package managers.commands;

import managers.CommandManager;
import managers.commands.Command;
import models.Dragon;

public class Help extends Command {
    public Help(){
        super("Help","Вывести справку по доступным командам");
    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        System.out.println("Список доступных команд:");
        String res = "";
        for(Command cmd: commandManager.getCommands().values()){
            res += cmd.getName() + ": " + cmd.getDescription() + '\n';
        }
        return res;
    }
}
