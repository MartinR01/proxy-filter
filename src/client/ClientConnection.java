package client;

import common.KMP;
import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientConnection {
    public static final int PORT = 80;

    private final Socket socket;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    public ClientConnection(String hostname, ServerClientMediator mediator) throws IOException {
        this.socket = new Socket(hostname, PORT);
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.mediator = mediator;
    }

    public void receiveMessage(Request request){
        try {
            writeRequest(request);
            mediator.messageServer(readResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response readResponse(){
        byte[] buffer = new byte[4096];

        StringBuilder stringBuilder = new StringBuilder();
        int endIndex = -1;

        Response response = null;

        int total = 0;
        byte[] contentField = null;

        KMP kmp = new KMP("\r\n\r\n");
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
        } catch (IOException e){
            e.printStackTrace();
        }

        if(response != null){
            response.setBody(contentField);
        }
        return response;
    }

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
