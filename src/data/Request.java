package data;

/**
 * Request object represents one parsed HTTP request
 */
public class Request {
    public final String data;
    public String host;

    /**
     * Constructs the object and parses the headers.
     * @param data Received HTTP request
     * @throws IllegalArgumentException may be thrown in case the request does not contain
     *      at least one newline character and a space.
     */
    public Request(String data) throws IllegalArgumentException{
        if (!data.contains("\r\n") && data.contains(" ")){
            throw new IllegalArgumentException("Invalid data: '" + data + "'");
        }
        System.out.println("---- New Request ----\n'"+data+"'");
        this.data = data;
        this.host = data.split("\r\n")[1].split(" ")[1];
    }
}
