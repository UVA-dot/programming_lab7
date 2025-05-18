package managers.commands;

import models.Dragon;
import managers.CollectionManager;

public class Max_by_weight extends Command {
    public Max_by_weight(){
        super("Max_by_weight", "Выводит любой элемент коллекции, значение поля weight, которого является максимальным");
    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        return collectionManager.max_by_weight();
    }
}
