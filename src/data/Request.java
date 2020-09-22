package data;

public class Request {
    public final String data;
    public final String host;

    public Request(String data){
        this.data = data;
        this.host = data.split("\r\n")[1].split(" ")[1];
    }
}
