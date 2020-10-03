package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Server is responsible for accepting the connection from clients.
 */
public class Server {
    /** Port number where the proxy runs */
    public static final int SERVER_PORT = 8080;
    private static Server instance = null;

    private ServerSocket socket;
    private ArrayList<ServerConnection> connections;

    /**
     * Constructs serverSocket on the SERVER_PORT
     */
    private Server(){
        try {
            socket = new ServerSocket(SERVER_PORT);
            connections = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accepts incoming connections and stores them.
     */
    public void acceptConnection(){
        try {
            ServerConnection connection = new ServerConnection(socket.accept());
            connections.add(connection);
            connection.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Server class is a singleton.
     * @return instance of the singleton
     */
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
