package server;

import client.ServerClientMediator;
import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;

/**
 * The class ServerConnection is responsible for developing the connection to the server through
 * methods receiveMessage, readRequest and writeResponse.
 */
public class ServerConnection {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    /**
     *
     * @param socket this parameter is using for get input and output stream.
     * @throws IOException Exceptions handled in case of bad input or output.
     */
    public ServerConnection(Socket socket) throws IOException{
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.mediator = new ServerClientMediator(this);
    }

    /**
     *
     */
    public void run(){
        try {
            Request request = readRequest();
            if(request == null){
                return;
            }
            mediator.messageClient(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(Response response){
        try {
            writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        run();
    }

    public Request readRequest() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();

        try{
            while(line != null && !line.equals("")){
                stringBuilder.append(line).append("\r\n");
                line = reader.readLine();
            }
            if(stringBuilder.length() == 0){
                return null;
            }
            stringBuilder.append("\r\n");
        } catch (IOException e){
            e.printStackTrace();
        }

        return new Request(stringBuilder.toString());
    }

    public void writeResponse(Response response) throws IOException {
        writer.write(response.data);
        writer.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
