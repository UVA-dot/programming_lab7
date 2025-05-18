package managers.commands;

import managers.CollectionManager;
import models.Dragon;

public class Clear extends Command {
    public Clear(){
        super("Clear", "Очистить коллекцию");

    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        return collectionManager.clear();
    }
}
