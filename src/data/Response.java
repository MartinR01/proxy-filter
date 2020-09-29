package data;

/**
 * The class Response is responsible for showing the new response and storing into the variable this.data;
 */
public class Response {
    public final String data;

    /**
     * The constructor Response is responsible for showing the new response of data.
     * @param data this parameter is using for storing and showing the data.
     */
    public Response(String data){
        System.out.println("---- New Response ----\n'"+data+"'");
        this.data = data;
    }
}
