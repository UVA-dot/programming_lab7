package managers.commands;

import models.Dragon;

public class Authorization extends Command{
    public Authorization(){
        super("Authorization", "Авторизировать пользователя");
    }
    @Override
    public String execute(String arg, Dragon dragon, String password, String user, Integer id) {
        return collectionManager.authorization(user, password);
    }
}