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

    public ClientConnection(String hostname) throws IOException {
        this.socket = new Socket(hostname, PORT);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
                System.out.println(reader.read(buffer));
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
