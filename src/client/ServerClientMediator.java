package client;

import data.Request;
import data.Response;
import server.ServerConnection;

import java.io.IOException;
import java.util.HashMap;

/**
 * The class ServerClientMediator is working as a mediator between client and server through
 * using the methods messageClient and messageServer.
 */
public class ServerClientMediator {
    private final HashMap<String, ClientConnection> clientConnections;
    private final ServerConnection serverConnection;

    /**
     *
     * @param serverConnection this parameter is representing the object of server connection
     */
    public ServerClientMediator(ServerConnection serverConnection){
        this.clientConnections = new HashMap<>();
        this.serverConnection = serverConnection;
    }

    /**
     * This method messageClient is responsible for receiving the message form client.
     * And it's also handling the IOException in case of bad input.
     * @param request this parameter is storing the request of client
     * @throws IOException this handling the IOException in case of bad input.
     */
    public void messageClient(Request request) throws IOException {
         getConnection(request.host).receiveMessage(request);
    }

    /**
     * This method messageServer is responsible for receiving the response message of serverConnection
     * @param response this parameter is storing the response message into serverConnection
     */
    public void messageServer(Response response){
        // TODO preprocessing
        serverConnection.receiveMessage(response);
    }

    /**
     * This method getConnection is connected to the class ClientConnection and
     * it's checking if the client connection contains the hostname or not.
     * @param hostname this parameter containing the String input of the host.
     * @return it's returning the host name for client connection.
     * @throws IOException handling the IOException in case of bad input or output
     */
    private ClientConnection getConnection(String hostname) throws IOException {
        if(!clientConnections.containsKey(hostname)){
            ClientConnection con = new ClientConnection(hostname, this);
            clientConnections.put(hostname, con);
            return con;
        }
        return clientConnections.get(hostname);
    }
}
