package conn;

import msg.IMessageable;
import msg.Message;
import msg.ServerClientMediator;

import java.io.IOException;
import java.net.Socket;

/**
 * Socket wrapper for serving clients
 */
public class ServerConnection implements IMessageable {
    private final ServerClientMediator mediator;
    private final Connection connection;

    /**
     * Constructs new instance on a socket created by incoming connection to the Server
     * @param socket socket object, typically created by incoming connection to the Server
     * @throws IOException in case IO streams of the socket cannot be opened
     */
    public ServerConnection(Socket socket) throws IOException{
        this.connection = new Connection(socket);
        this.mediator = new ServerClientMediator(this);
    }

    /**
     * Reads an incoming request and sends it to the mediator
     */
    public void run(){
        try {
            Message request = connection.readMessage();
            if(request == null){
                return;
            }
            mediator.messageClient(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes received response to socket's output, then starts reading another request
     * @param response received response object
     */
    public void receiveMessage(Message response){
        connection.writeMessage(response);
        run();
    }

}
