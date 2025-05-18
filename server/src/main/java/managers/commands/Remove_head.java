package managers.commands;

import models.Dragon;
import managers.CollectionManager;

public class Remove_head extends Command {
    public Remove_head(){
        super("Remove_head", "Вывести первый элемент коллекции и удалить его");
    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        return collectionManager.remove_head(id);
    }
}
