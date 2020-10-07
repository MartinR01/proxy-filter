package messages;

/**
 * Request object represents one parsed HTTP request
 * Adds request-specific parsing functions to Message class.
 */
public class RequestMessage{
    private final Message message;

    /**
     * Constructs the object and parses the headers.
     * @param message Received HTTP request
     */
    public RequestMessage(Message message){
        this.message = message;
    }

    public RequestMessage(String headers){
        this(new Message(headers));
    }

    public Message getMessage() {
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

    /**
     * Represents host header
     */
    public class Host{
        /** standard HTTP port to open connection on */
        public static final int HTTP_PORT = 80;

        /** hostname, either url or ip address */
        public final String hostname;
        /** port number */
        public final int port;

        /**
         * Constructs the Host class
         * @param hostname hostname
         * @param port port number
         */
        public Host(String hostname, int port){
            this.hostname = hostname;
            this.port = port;
        }

        /**
         * Constructs the Host class with default HTTP port
         * @param hostname hostname
         */
        public Host(String hostname){
            this.hostname = hostname;
            this.port = HTTP_PORT;
        }
    }
}
