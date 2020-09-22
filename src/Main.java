import client.Client;
import client.ClientConnection;
import data.Request;
import data.Response;
import server.Server;
import server.ServerConnection;

import java.io.IOException;

public class Main{
    public static void main(String[] args){
        Server server = Server.getInstance();
        Client client = Client.getInstance();

        ServerConnection serverConnection = server.acceptConnection();
        Request request = serverConnection.readRequest();

        ClientConnection clientConnection = null;
        try {
            clientConnection = client.getConnection(request.host);
            clientConnection.writeRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = clientConnection.readResponse();
        System.out.print("'"+response.data+"'");

        try {
            serverConnection.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
