package data;

public class Request {
    public final String data;
    public String host;

    public Request(String data) throws IllegalArgumentException{
        if (!data.contains("\r\n") && data.contains(" ")){
            throw new IllegalArgumentException("Invalid data: '" + data + "'");
        }
        System.out.println("---- New Request ----\n'"+data+"'");
        this.data = data;
        this.host = data.split("\r\n")[1].split(" ")[1];
    }
}
