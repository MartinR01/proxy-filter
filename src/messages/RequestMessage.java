package messages;

import com.ClientConnection;

/**
 * Request object represents one parsed HTTP request
 */
public class RequestMessage extends AMessage{

    /**
     * Constructs the object and parses the headers.
     * @param headers Received HTTP request
     */
    public RequestMessage(String headers){
        super(headers);
    }

    // TODO make general interface for all headers - that allows to get the raw string value .... can be then used in the HashMap
    public Host getHost(){
        String host = getHeaderValue("host");
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
            this.port = ClientConnection.HTTP_PORT;
        }
    }
}
