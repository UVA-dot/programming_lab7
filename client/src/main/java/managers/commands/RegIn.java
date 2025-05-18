package managers.commands;

public class RegIn extends Command {
    public RegIn(){
        super("RegIn", "Зарегистрировать пользователя");
    }
    public String execute(String arg){
        return collectionManager.regIn();
    }
}
