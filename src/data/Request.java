package data;

import client.ClientConnection;

import java.util.HashMap;

/**
 * Request object represents one parsed HTTP request
 */
public class Request {
    private String status;
    private HashMap<String, String> headers;
    private byte[] body;

    /**
     * Constructs the object and parses the headers.
     * @param headers Received HTTP request
     * @throws IllegalArgumentException may be thrown in case the request does not contain
     *      at least one newline character and a space.
     */
    public Request(String headers) throws IllegalArgumentException{
        System.out.println("---- New request ----\n'"+headers+"'");
        String[] all = headers.split("\r\n");

        this.headers = new HashMap<>();
        this.status = all[0];

        for (int i = 1; i < all.length; i++){
            if (all[i].contains(": ")){
                String[] keyValue = all[i].split(": ");
                this.headers.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public String getHeaderValue(String name){
        // TODO needs case insensitive matching, because HTTP allows for different types
        return headers.get(name);
    }

    // TODO make general interface for all headers - that allows to get the raw string value .... can be then used in the HashMap
    public Host getHost(){
        String host = headers.get("Host");
        if(host.contains(":")){
            String[] ar = host.split(":");
            return new Host(ar[0], Integer.parseInt(ar[1]));
        } else {
            return new Host(host);
        }
    }

    public byte[] getBody(){
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getContentSize(){
        String contentSizeString = getHeaderValue("Content-Length");
        if (contentSizeString == null){
            return -1;
        }
        return Integer.parseInt(contentSizeString);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(status + "\r\n");
        for (String key : headers.keySet()){
            stringBuilder.append(key + ": "+ headers.get(key) + "\r\n");
        }
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
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
