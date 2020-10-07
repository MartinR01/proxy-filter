package messages;

/**
 * Response object represents one parsed HTTP response
 */
public class ResponseMessage extends AMessage{

    /**
     * Constructs the object and parses the headers.
     * @param headers Received HTTP response
     */
    public ResponseMessage(String headers){
        super(headers);
    }

    public String getContentType(){
        return getHeaderValue("content-type");
    }
}
