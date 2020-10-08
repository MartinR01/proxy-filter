package msg;

/**
 * Response object represents one parsed HTTP response
 * Adds response-specific parsing functions to Message class.
 */
public class ResponseMessage{
    private final Message message;

    /**
     * Constructs the object and parses the headers.
     * @param message Received HTTP message
     */
    public ResponseMessage(Message message){
        this.message = message;
    }

    public ResponseMessage(String headers){
        this(new Message(headers));
    }

    public byte[] getBody(){
        return message.getBody();
    }

    public void setBody(byte[] body){
        message.setBody(body);
    }

    public Message getMessage() {
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
