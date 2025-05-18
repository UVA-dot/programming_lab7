package managers.commands;

import managers.CollectionManager;
import models.Dragon;

public class Info extends Command {
    public Info(){
        super("Info","Вывести информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        return collectionManager.printInfo();
    }
}
