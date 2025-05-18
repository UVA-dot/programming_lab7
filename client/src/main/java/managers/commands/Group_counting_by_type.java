package managers.commands;

import models.Dragon;
import managers.CollectionManager;
import models.DragonType;

public class Group_counting_by_type extends Command{
    public Group_counting_by_type(){
        super("Group_counting_by_type", "сгруппировать элементы коллекции по значению поля type, вывести количество элементов в каждой группе");
    }
    @Override
    public String execute(String arg){
        return collectionManager.group_by();
    }
}
