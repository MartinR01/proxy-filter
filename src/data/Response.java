package data;

public class Response {
    private String data;

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
