package messages;

import com.ClientConnection;

/**
 * Request object represents one parsed HTTP request
 */
public class RequestMessage{
    /** standard HTTP port to open connection on */
    public static final int HTTP_PORT = 80;
    private AMessage message;

    /**
     * Constructs the object and parses the headers.
     * @param headers Received HTTP request
     */
    public RequestMessage(String headers){
        this(new AMessage(headers));
    }

    public RequestMessage(AMessage message){
        this.message = message;
    }

    public AMessage getMessage() {
        return message;
    }

    public byte[] getBody(){
        return message.getBody();
    }

    public void setBody(byte[] body){
        message.setBody(body);
    }

    public int getContentSize(){
        return message.getContentSize();
    }

    @Override
    public String toString() {
        return message.toString();
    }

    // TODO make general interface for all headers - that allows to get the raw string value .... can be then used in the HashMap
    public Host getHost(){
        String host = message.getHeaderValue("host");
        if(host.contains(":")){
            String[] ar = host.split(":");
            return new Host(ar[0], Integer.parseInt(ar[1]));
        } else {
            return new Host(host);
        }
    }

    public class Host{
        public final String hostname;
        public final int port;

        public Host(String hostname, int port){
            this.hostname = hostname;
            this.port = port;
        }

        public Host(String hostname){
            this.hostname = hostname;
            this.port = HTTP_PORT;
        }
    }
}
