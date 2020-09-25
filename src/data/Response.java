package data;

import java.util.HashMap;

public class Response {
    private String data;

    private String status;
    private HashMap<String, String> headers;
    private String body;

    public Response(String data){
        System.out.println("---- New Response ----\n'"+data+"'");
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
