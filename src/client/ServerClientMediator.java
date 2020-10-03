package client;

import data.Request;
import data.Response;
import server.ServerConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ServerClientMediator facilitates the communication between server side and client side.
 * Also can create additional client sockets if the target connection is not yet established.
 */
public class ServerClientMediator {
    private final HashMap<String, ClientConnection> clientConnections;
    private final ServerConnection serverConnection;

    /**
     * Constructs the ServerClientMediator object
     * @param serverConnection individual connection with client
     */
    private ArrayList<Filter> filters;

    public ServerClientMediator(ServerConnection serverConnection){
        this.clientConnections = new HashMap<>();
        this.serverConnection = serverConnection;

        filters = new ArrayList<>();
        filters.add(new Filter("Smiley", "Trolly"));
        filters.add(new Filter("Stockholm", "Linköping"));
        filters.add(new Filter("smiley.jpg", "trolly.jpg"));
    }

    /**
     * Sends message to client-side
     * @param request request received typically by the server-side
     * @throws IOException see getConnection
     */
    public void messageClient(Request request) throws IOException {
         getConnection(request.host).receiveMessage(request);
    }

    /**
     * Sends message to server-side
     * @param response response received typically by the client-side
     */
    public void messageServer(Response response){
        filters.forEach(f -> f.filter(response));
        serverConnection.receiveMessage(response);
    }

    /**
     * Gets connection object to the passed host.
     * Creates the connection if none has yet been established.
     * @param hostname url or IP address of host to connect to
     * @return connection object representing passed hostname
     * @throws IOException may be thrown in case Socket object cannot be created for the host
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
