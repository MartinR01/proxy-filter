package server;

import client.ServerClientMediator;
import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;

/**
 * The class ServerConnection is socket wrapper for serving clients
 */
public class ServerConnection {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    /**
     * Constructs new instance on a socket created by incoming connection to the Server
     * @param socket socket object, typically created by incoming connection to the Server
     * @throws IOException in case IO streams of the socket cannot be opened
     */
    public ServerConnection(Socket socket) throws IOException{
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.mediator = new ServerClientMediator(this);
    }

    /**
     * Reads an incoming request and sends it to the mediator
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

    /**
     * Writes received response to socket's output, then starts reading another request
     * @param response received response object
     */
    public void receiveMessage(Response response){
        try {
            writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        run();
    }

    /**
     * Reads incoming request and encapsulates it into a Request object
     * @return parsed request
     * @throws IOException see {@link Request}
     */
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

    /**
     * Writes a Response object into socket's output stream
     * @param response HTTP response represented by the Response object
     * @throws IOException thrown by BufferedWriter
     */
    public void writeResponse(Response response) throws IOException {
        if(response != null){
            writer.write(response.toString());
            writer.flush();
            if (response.getBody() != null){
                socket.getOutputStream().write(response.getBody());
            }
        } else {
            // TODO what about removing this branch?
            writer.write(-1);
            writer.flush();
        }
//        writer.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
