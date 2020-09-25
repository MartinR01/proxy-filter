package client;

import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;

/**
 * The Class ClientConnection is responsible for developing the connection to client through
 * methods receiveMessage, readResponse and writeRequest.
 *
 */

public class ClientConnection {
    public static final int PORT = 80;

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    /**
     *
     * @param hostname it's passing the port number, which is actually 80 in our case (for HTTP response)
     * @param mediator this parameter is working as a mediator and it's responsible for getting the
     *                 bit codes of text or image and converting them into the desired text or image.
     * @throws IOException we handled the situation if there is any wrong at the time of Input or Output.
     */
    public ClientConnection(String hostname, ServerClientMediator mediator) throws IOException {
        this.socket = new Socket(hostname, PORT);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.mediator = mediator;
    }

    /**
     * The method receiveMessage is receiving the request from the client and reading the
     * response from the server as well. Moreover, it's handling the IOException.
     * @param request this parameter is responsible for getting the request from client of the proxy.
     */
    public void receiveMessage(Request request){
        try {
            writeRequest(request);
            mediator.messageServer(readResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method readResponse is responsible for taking and reading the response from user.
     * IOExceptions are handled as well.
     * @return this method is returning the response buffer in terms of bits.
     */

    public Response readResponse(){
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        int contentLength = 0;

        try{
            while(!(line = reader.readLine()).equals("")){
                stringBuilder.append(line).append("\r\n");
                if (line.startsWith("Content-Length")){
                    contentLength = Integer.parseInt(line.split(" ")[1]);
                }
            }
            stringBuilder.append("\r\n");

            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                reader.read(buffer);
                stringBuilder.append(buffer);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        return new Response(stringBuilder.toString());
    }

    /**
     * The method writeRequest is responsible for writing the request in the Request.java class
     * @param request this parameter is storing the request data from user and passing into the Request class.
     * @throws IOException here IOException is handled in case of wrong input while writing the request.
     */
    public void writeRequest(Request request) throws IOException {
        writer.write(request.data);
        writer.flush();
    }

    /**
     * The default method of java to remove the garbage
     * @throws Throwable show the message if any inappropriate thing accrue.
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }

}
