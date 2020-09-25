package client;

import data.Request;
import data.Response;
import server.ServerConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerClientMediator {
    private final HashMap<String, ClientConnection> clientConnections;
    private final ServerConnection serverConnection;

    private ArrayList<Filter> filters;

    public ServerClientMediator(ServerConnection serverConnection){
        this.clientConnections = new HashMap<>();
        this.serverConnection = serverConnection;

        filters = new ArrayList<>();
//        filters.add(new Filter("Smiley", "Trolly"));
//        filters.add(new Filter("Stockholm", "Linköping"));
//        filters.add(new Filter("smiley.jpg", "trolly.jpg"));
    }

    public void messageClient(Request request) throws IOException {
         getConnection(request.host).receiveMessage(request);
    }

    public void messageServer(Response response){
        filters.forEach(f -> f.filter(response));
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
