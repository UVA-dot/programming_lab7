package managers.commands;

public class Out extends Command{
    public Out(){
        super("Out", "Разлогин пользователя");
    }
    @Override
    public String execute(String arg) {
        return collectionManager.out();
    }
}