package client;

import common.KMP;
import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * ClientConnection is responsible for developing the connection to client
 */
public class ClientConnection {
    /** standard HTTP port to open connection on */
    public static final int HTTP_PORT = 80;

    private Socket socket;
//    private final BufferedWriter writer;
    private PrintWriter printWriter;

    private ServerClientMediator mediator;

    /**
     * Constructs the ClientConnection object
     * @param hostname url or IP address of host to connect to
     * @param mediator mediator object for communication with the server side
     * @throws IOException construction of Socket object may throw this exception
     */
    public ClientConnection(String hostname, ServerClientMediator mediator) throws IOException {
        this.socket = new Socket(hostname, HTTP_PORT);
//        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream());
        this.mediator = mediator;
//        this.socket.setKeepAlive(true);
//        System.out.println("Is keep alive: "+socket.getKeepAlive());
    }

    public void connect() throws IOException {
        System.out.println(socket.getPort());
        this.socket = new Socket("zebroid.ida.liu.se", 80);
        this.printWriter = new PrintWriter(socket.getOutputStream());
//        this.socket.setKeepAlive(true);
//        System.out.println(socket.isOutputShutdown()+" " +socket.get()+" " +socket.isClosed());
    }

    /**
     * The method receiveMessage is receiving the request from the client and reading the
     * response from the server as well. Moreover, it's handling the IOException.
     * @param request this parameter is responsible for getting the request from client of the proxy.
     */
    public void receiveMessage(Request request){
        try {
            writeRequest(request);
            readResponse();
//            mediator.messageServer(readResponse());
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
        if (socket.isClosed()){
            System.out.println("+++++ CLOSED ++++++");
            try {
                connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] buffer = new byte[4096];

        StringBuilder stringBuilder = new StringBuilder();
        int endIndex = -1;

        Response response = null;

        int total = 0;
        byte[] contentField = null;

        KMP kmp = new KMP("\r\n\r\n");
        long startTime = System.currentTimeMillis();
        try {
            int read;
            while ((read = socket.getInputStream().read(buffer)) != -1){
                if (endIndex == -1){
                    endIndex = kmp.search(buffer, read);
                    if (endIndex == -1){
                        stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, read)));
                    } else {
                        stringBuilder.append(new String((Arrays.copyOfRange(buffer, 0, endIndex - 1))));

                        response = new Response(stringBuilder.toString());
                        int contentSize = response.getContentSize();

                        contentField = new byte[contentSize];

                        for (int i = endIndex + 1, j = 0; i < read; i++, j++){
                            contentField[j] = buffer[i];
                            total++;
                        }
                    }
                } else {
                    for(int i = 0, j = total; i < read; i++, j++){
                        contentField[j] = buffer[i];
                        total++;
                    }
                }

            }
//            socket.close();
//            printWriter.flush();
//            printWriter.close();
//            System.out.println("Delay: "+ (System.currentTimeMillis() - startTime));
        } catch (IOException e){
            e.printStackTrace();
        }

        if(response != null){
            response.setBody(contentField);
        }
        return response;
    }

    /**
     * Writes a Request object into socket's output stream
     * @param request HTTP request represented by the Request object
     * @throws IOException thrown by BufferedWriter
     */
    public void writeRequest(Request request) throws IOException {
//        System.out.println("Request: '"+request.data+"'");
//        writer.write(request.data);
//        writer.flush();
        printWriter.write(request.data);
        printWriter.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
