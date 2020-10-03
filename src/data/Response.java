package data;

/**
 * Response object represents one parsed HTTP response
 */
public class Response {
    public final String data;

    /**
     * Constructs the object and parses the headers.
     * @param data Received HTTP response
     */
    public Response(String data){
        System.out.println("---- New Response ----\n'"+data+"'");
        this.data = data;
    }
}
