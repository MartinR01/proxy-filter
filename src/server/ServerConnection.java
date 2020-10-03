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
     * The method ServerConnection is responsible for getting the data from ServerClientMediator class.
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
     * The method run is responsible for storing the value from readRequest method and assigning that
     * request value to mediator.
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
     * The method is responsible for receiving the response message and then
     * storing into the method writeResponse.
     * @param response this parameter is using for storing the response message.
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
     * The method readRequest is responsible for reading reading then storing into new variable.
     * @return It returns the request into the stringRequest and then passing into the class Request.
     * @throws IOException It is handling the input output  exception.
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
     *The method writeResponse is writing the response into the write variable.
     * @param response this paramater is using for passing the response data into the writer variable
     * @throws IOException Input Output Exceptions are handled.
     */
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
