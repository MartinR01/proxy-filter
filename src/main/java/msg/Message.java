package msg;

import java.util.HashMap;

/**
 * Represents one message passing through the proxy.
 */
public class Message {
    /** Standard HTTP newline sequence */
    public static final String NEWLINE = "\r\n";
    /** Standard HTTP ending of headers part of message */
    public static final String HEADERS_END = NEWLINE + NEWLINE;
    /** Standard HTTP separator of header name and value */
    public static final String HEADER_SEP = ": ";

    private final String status;
    private final HashMap<String, String> headers;
    private byte[] body;

    /**
     * Constructs the object and parses the headers.
     * @param headers Received HTTP headers
     */
    public Message(String headers){
        System.out.println("---- New Message ----\n'"+headers+"'");
        String[] all = headers.split(NEWLINE);

        this.headers = new HashMap<>();
        this.status = all[0];

        for (int i = 1; i < all.length; i++){
            if (all[i].contains(HEADER_SEP)){
                String[] keyValue = all[i].split(HEADER_SEP);
                this.headers.put(keyValue[0].toLowerCase(), keyValue[1]);
            }
        }
    }

    /**
     * Gets value of specified header
     * @param name header name
     * @return header value
     */
    protected String getHeaderValue(String name){
        return headers.get(name);
    }

    /**
     * Gets and parses the value of Content-Length header
     * @return content-length value or -1 if the header was not found
     */
    public int getContentSize(){
        String contentSizeString = getHeaderValue("content-length");
        if (contentSizeString == null){
            return -1;
        }
        return Integer.parseInt(contentSizeString);
    }

    public void setBody(byte[] body){
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(status + NEWLINE);
        for (String key : headers.keySet()){
            stringBuilder.append(key + HEADER_SEP+ headers.get(key) + NEWLINE);
        }
        stringBuilder.append(NEWLINE);

        return stringBuilder.toString();
    }
}
