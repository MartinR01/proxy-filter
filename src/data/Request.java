package data;

/**
 * The Class Request is responsible for showing the new request data and passing inti the variable host.
 */
public class Request {
    public final String data;
    public String host;

    /**
     * The constructor Request is responsible for checking the data request and showing the new request of data.
     * @param data this parameter is using for showing the data for the new request.
     * @throws IllegalArgumentException Handling the errors in case of illegal arguments.
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
