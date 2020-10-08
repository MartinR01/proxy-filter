package conn;

import msg.IMessageable;
import msg.Message;
import msg.RequestMessage;
import msg.ServerClientMediator;

import java.io.IOException;
import java.net.Socket;

/**
 * Socket wrapper for connecting to remote servers
 */
public class ClientConnection implements IMessageable {
    private final ServerClientMediator mediator;
    private final Connection connection;

    /**
     * Constructs the ClientConnection object
     * @param hostname url or IP address of host to connect to
     * @param mediator mediator object for communication with the server side
     * @throws IOException construction of Socket object may throw this exception
     */
    public ClientConnection(RequestMessage.Host hostname, ServerClientMediator mediator) throws IOException {
        this.connection = new Connection(new Socket(hostname.hostname, hostname.port));
        this.mediator = mediator;
    }

    /**
     * The method receiveMessage is receiving the request from the client and reading the
     * response from the server as well.
     * @param message this parameter is responsible for getting the request from client of the proxy.
     */
    public void receiveMessage(Message message){
        connection.writeMessage(message);
        mediator.messageServer(connection.readMessage());
    }
}
