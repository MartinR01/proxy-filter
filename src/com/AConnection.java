package com;

import commons.KMP;
import messages.AMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public abstract class AConnection {
    private final Socket socket;
    private final PrintWriter printWriter;

    public AConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream());
    }

    /**
     * The method readResponse is responsible for taking and reading the response from user.
     * IOExceptions are handled as well.
     * @return returns received response parsed as a Response object
     */
    public AMessage readResponse(){
        byte[] buffer = new byte[4096];

        StringBuilder stringBuilder = new StringBuilder();
        int endIndex = -1;

        AMessage response = null;

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
                        if (read > -1){
                            stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, read)));
                        }
                    } else {
                        stringBuilder.append(new String((Arrays.copyOfRange(buffer, 0, endIndex - 1))));

                        response = new AMessage(stringBuilder.toString());
                        contentSize = response.getContentSize();

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
        if(response != null){
            response.setBody(contentField);
        }
        return response;
    }

    /**
     * Writes a Request object into socket's output stream
     * @param request HTTP request represented by the Request object
     */
    public void writeRequest(AMessage request) {
        printWriter.write(request.toString());
        printWriter.flush();
        if (request.getBody() != null){
            try {
                socket.getOutputStream().write(request.getBody());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        socket.close();
    }
}
