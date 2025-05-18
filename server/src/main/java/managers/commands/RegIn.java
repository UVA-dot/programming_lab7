package managers.commands;

import models.Dragon;

public class RegIn extends Command {
    public RegIn(){
        super("RegIn", "Зарегистрировать пользователя");
    }
    public String execute(String arg, Dragon dragon, String password, String user, Integer id){
        return collectionManager.regIn(user, password);
    }
}
