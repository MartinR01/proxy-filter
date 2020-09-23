package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    public static final int PORT = 8080;
    private static Server instance = null;

    private ServerSocket socket;
    private ArrayList<ServerConnection> connections;

    private Server(){
        try {
            socket = new ServerSocket(PORT);
            connections = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection(){
        try {
            ServerConnection connection = new ServerConnection(socket.accept());
            connections.add(connection);
            connection.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Server getInstance(){
        if (instance == null){
            instance = new Server();
        }
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
