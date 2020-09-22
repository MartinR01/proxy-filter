package server;

import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;

public class ServerConnection {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerConnection(Socket socket) throws IOException{
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public Request readRequest() {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try{
            while(!(line = reader.readLine()).equals("")){
                stringBuilder.append(line).append("\r\n");
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
