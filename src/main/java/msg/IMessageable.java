package msg;

/**
 * Classes Implementing this interface can accept and handle incoming Messages from other classes
 */
public interface IMessageable {
    void receiveMessage(Message message);
}
