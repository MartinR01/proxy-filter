package client;

import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;

/**
 * ClientConnection is responsible for developing the connection to client
 */
public class ClientConnection {
    /** standard HTTP port to open connection on */
    public static final int HTTP_PORT = 80;

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    /**
     * Constructs the ClientConnection object
     * @param hostname url or IP address of host to connect to
     * @param mediator mediator object for communication with the server side
     * @throws IOException construction of Socket object may throw this exception
     */
    public ClientConnection(String hostname, ServerClientMediator mediator) throws IOException {
        this.socket = new Socket(hostname, HTTP_PORT);
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
     * @return returns received response parsed as a Response object
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
     * Writes a Request object into socket's output stream
     * @param request HTTP request represented by the Request object
     * @throws IOException thrown by BufferedWriter
     */
    public void writeRequest(Request request) throws IOException {
        writer.write(request.data);
        writer.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }

}
