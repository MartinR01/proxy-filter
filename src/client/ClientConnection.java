package client;

import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    public static final int PORT = 80;

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    public ClientConnection(String hostname, ServerClientMediator mediator) throws IOException {
        this.socket = new Socket(hostname, PORT);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
