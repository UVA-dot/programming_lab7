package managers.commands;

public class Authorization extends Command{
    public Authorization(){
        super("Authorization", "Авторизировать пользователя");
    }
    @Override
    public String execute(String arg) {
        return collectionManager.authorization();
    }
}