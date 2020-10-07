package com;

import com.ServerClientMediator;
import commons.KMP;
import messages.AMessage;
import messages.RequestMessage;
import messages.ResponseMessage;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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
            RequestMessage request = readRequest();
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
    public void receiveMessage(ResponseMessage response){
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
     * @throws IOException see {@link RequestMessage}
     */
    public RequestMessage readRequest() throws IOException {
        byte[] buffer = new byte[4096];

        StringBuilder stringBuilder = new StringBuilder();
        int endIndex = -1;

        RequestMessage request = null;

        int total = 0;
        byte[] contentField = null;

        KMP kmp = new KMP(AMessage.HEADERS_END);
        try {
            int read;
            int contentSize = -1;

            do{
                read = socket.getInputStream().read(buffer);
                if (endIndex == -1){
                    endIndex = kmp.search(buffer, read);
                    if (endIndex == -1){
                        if(read > 0) {
                            System.out.println("Buffer: " + Arrays.toString(buffer));
                            stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, read)));
                        }
                    } else {
                        stringBuilder.append(new String((Arrays.copyOfRange(buffer, 0, endIndex - 1))));

                        request = new RequestMessage(stringBuilder.toString());
                        contentSize = request.getContentSize();

                        if(contentSize == -1){
                            break;
                        }

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
                // if content size specified, make sure to read the whole payload, otherwise read while buffer is full
            } while((contentSize != -1) ? (total != contentSize) : (read == buffer.length));
        } catch (IOException e){
            e.printStackTrace();
        }
        if(request != null){
            request.setBody(contentField);
        }
        return request;
    }

    /**
     * Writes a Response object into socket's output stream
     * @param response HTTP response represented by the Response object
     * @throws IOException thrown by BufferedWriter
     */
    public void writeResponse(ResponseMessage response) throws IOException {
        if(response != null){
            writer.write(response.toString());
            writer.flush();
            if (response.getBody() != null){
                socket.getOutputStream().write(response.getBody());
                socket.getOutputStream().flush();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
