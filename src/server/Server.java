package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * The class Server is responsible for accepting the connection from server.
 */
public class Server {
    public static final int PORT = 8080;
    private static Server instance = null;

    private ServerSocket socket;
    private ArrayList<ServerConnection> connections;

    /**
     * constructor Server is storing the ServerSocket PORT into the socket variable.
     * Input Output Exceptions are handled.
     */
    private Server(){
        try {
            socket = new ServerSocket(PORT);
            connections = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method acceptConnection is responsible for establishing the server connection.
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
     * The method getInstance is responsible for checking the instance and assigning it to the server.
     * @return its returning the static variable instance.
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
