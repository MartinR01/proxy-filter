package client;

import data.Request;
import data.Response;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;

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
        int matched = 0;
        char[] text = "\r\n\r\n".toCharArray();
        int[] error = new int[]{0, 0, 1, 2};
        StringBuilder stringBuilder = new StringBuilder();
        int contentOffset = 0;
        boolean found = false;

        Response response = null;

        String headers;
        int total = 0;
        byte[] contentField = null;
        // TODO i and read can be merged to simplify code, this way we can get rid of contentOffset as well
        try {
            while ((read = socket.getInputStream().read(buffer)) != -1){
                if (!found){
                    for(int i = 0; i < read; i++){
                        if (buffer[i] == text[matched]){
                            matched++;
                            if(matched == text.length){
                                System.out.println("---------------- FOUND ------------");
                                matched = 0;
                                stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, i)));
                                found = true;
                                i += 1;
                                contentOffset = i;
                                break;
                            }
                        } else {
                            matched = (matched > 0) ? error[matched - 1] : 0;
                        }
                    }
                    stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, read)));
                }
                if (contentField == null){
                    response = new Response(stringBuilder.toString(), true);
                    int contentSize = Integer.parseInt(response.getHeaderValue("Content-Length"));
                    System.out.println("content size: "+ contentSize);

                    contentField = new byte[contentSize];

                    for (int i = contentOffset, j = 0; i < read; i++, j++){
                        contentField[j] = buffer[i];
                        total++;
                    }
                    contentOffset = 0;
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
            System.out.println("+++++++++++++++ RESPONSE ++++++++++++");
//            byte[] decoded = Base64.getDecoder().decode(contentField);
//            System.out.println(new String(decoded));
            System.out.print("first bytes: "); for (int i = 0; i < 8; i++) System.out.print((contentField[i]) + " ");
            System.out.println();
            System.out.print("last bytes: "); for (int i = contentField.length-9; i < contentField.length; i++) System.out.print((contentField[i]) + " ");
            System.out.println();
            System.out.println("total bytes fo content read= "+total);
            System.out.println("real content size "+contentField.length);
            System.out.println("content size: "+new String(contentField).length());
//            for (int i = new String(contentField).length(); i < contentField.length; i++){
//                System.out.print(contentField[i]);
//            }
//            System.out.println();
            System.out.println("++++++++++++++++++++++++++++++++++++++");
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
