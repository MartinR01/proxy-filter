package data;

public class Response {
    public final String data;

    public Response(String data){
        System.out.println("---- New Response ----\n'"+data+"'");
        this.data = data;
    }
}
