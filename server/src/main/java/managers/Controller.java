package managers;

import models.Dragon;

import java.io.FileNotFoundException;

public class Controller {
    CommandManager commandManager = CommandManager.getCommandManager();
    public String run(String command, Dragon dragon, String line, String user, String password, Integer id) throws FileNotFoundException {

        switch (command) {
            case "info", "help", "exit", "show", "clear", "save", "head", "remove_head", "max_by_weight", "history", "group_by":

                return (commandManager.getCommand(command).execute(command, dragon, null, null, id));

            case "remove_by_id", "update_id":
                return (commandManager.getCommand(command).execute(command, dragon, null, null, id));

            case "add":

                return (commandManager.getCommand(command).execute(command, dragon, null, null, id));

            case "filter":
                return (commandManager.getCommand(command).execute(line, dragon, password, user, null));
            case "execute_script":
//                System.out.println(control.getCommand(command).execution(line,null));
                break;
            default:

                break;


        }
        return "";
    }

    public void createCommand() {
    }
}
