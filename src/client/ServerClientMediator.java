package client;

import data.Request;
import data.Response;
import server.ServerConnection;

import java.io.IOException;
import java.util.HashMap;

public class ServerClientMediator {
    private final HashMap<String, ClientConnection> clientConnections;
    private final ServerConnection serverConnection;

    public ServerClientMediator(ServerConnection serverConnection){
        this.clientConnections = new HashMap<>();
        this.serverConnection = serverConnection;
    }

    public void messageClient(Request request) throws IOException {
         getConnection(request.host).receiveMessage(request);
    }

    public void messageServer(Response response){
        // TODO preprocessing
        serverConnection.receiveMessage(response);
    }

    private ClientConnection getConnection(String hostname) throws IOException {
        if(!clientConnections.containsKey(hostname)){
            ClientConnection con = new ClientConnection(hostname, this);
            clientConnections.put(hostname, con);
            return con;
        }
        return clientConnections.get(hostname);
    }
}
