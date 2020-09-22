package client;

import java.io.IOException;
import java.util.HashMap;

public class Client {
    private static Client instance;

    private final HashMap<String, ClientConnection> connections;

    private Client(){
        connections = new HashMap<>();
    }

    public ClientConnection getConnection(String hostname) throws IOException {
        if(!connections.containsKey(hostname)){
            ClientConnection con = new ClientConnection(hostname);
            connections.put(hostname, con);
            return con;
        }
        return connections.get(hostname);
    }

    public static Client getInstance(){
        if (instance == null){
            instance = new Client();
        }
        return instance;
    }
}
