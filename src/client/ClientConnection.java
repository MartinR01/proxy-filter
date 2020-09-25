package client;

import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientConnection {
    public static final int PORT = 80;

    private final Socket socket;
//    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final ServerClientMediator mediator;

    public ClientConnection(String hostname, ServerClientMediator mediator) throws IOException {
        this.socket = new Socket(hostname, PORT);
//        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        int read;
        byte[] buffer = new byte[4096];
//        int matched = 0;
//        char[] text = "\r\n\r\n".toCharArray();
//        int[] error = new int[]{0, 0, 1, 2};
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((read = socket.getInputStream().read(buffer)) != -1){
                stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, read)));
//                for(int i = 0; i < read; i++){
//                    if (buffer[i] == text[matched]){
//                        matched++;
//                        if(matched == text.length){
//                            System.out.println("---------------- FOUND ------------");
//                            matched = 0;
//                            stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, i)));
//                        }
//                    } else {
//                        matched = (matched > 0) ? error[matched-1] : 0;
//                    }
//                    stringBuilder.append(new String(buffer));
//                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("-----------------------read ------------------------------\n"+stringBuilder.toString());

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
