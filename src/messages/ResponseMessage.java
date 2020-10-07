package messages;

/**
 * Response object represents one parsed HTTP response
 */
public class ResponseMessage{
    private AMessage message;

    /**
     * Constructs the object and parses the headers.
     * @param headers Received HTTP response
     */
    public ResponseMessage(String headers){
        this(new AMessage(headers));
    }

    public ResponseMessage(AMessage message){
        this.message = message;
    }

    public byte[] getBody(){
        return message.getBody();
    }

    public void setBody(byte[] body){
        message.setBody(body);
    }

    public AMessage getMessage() {
        return message;
    }

    public int getContentSize(){
        return message.getContentSize();
    }

    public String getStatus(){
        return message.getStatus();
    }

    public String getContentType(){
        return message.getHeaderValue("content-type");
    }

    @Override
    public String toString() {
        return message.toString();
    }
}
