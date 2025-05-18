package request;

import models.Dragon;

public class CommandRequest extends Request {
    public Integer id;
    public String line;
    public Dragon dragon;
    public String user;
    public String password;
    public CommandRequest(String name) {
        super(name);
    }
    public CommandRequest(String name, Integer id){
        super(name);
        this.id=id;
    }
    public CommandRequest(String name, Integer id, Dragon dragon){
        super(name);
        this.id=id;
        this.dragon=dragon;
    }
    public CommandRequest(String name, String line){
        super(name);
        this.line=line;
    }
    public CommandRequest(String name, String user, String password){
        super(name);
        this.user = user;
        this.password = password;
    }
    public CommandRequest(String name, String line, Integer id){
        super(name);
        this.line = line;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getLine() {
        return line;
    }

    public Dragon getDragon() {
        return dragon;
    }
    public String getUser(){
        return user;
    }
    public String getPassword(){
        return password;
    }

}
