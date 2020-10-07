package com;

import commons.KMP;
import messages.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

/**
 * Handles socket connection - both HTTP-specific reading and writing.
 */
public abstract class AConnection {
    private final Socket socket;
    private final PrintWriter printWriter;

    /**
     * Creates connection object using specified socket
     * @param socket connected socket
     * @throws IOException thrown when there is an error constructing IO streams for the socket.
     */
    public AConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream());
    }

    /**
     * Reads incoming HTTP message
     * @return received HTTP message
     */
    public Message readMessage(){
        byte[] buffer = new byte[4096];

        StringBuilder stringBuilder = new StringBuilder();
        int endIndex = -1;

        Message response = null;

        int total = 0;
        byte[] contentField = null;

        KMP kmp = new KMP(Message.HEADERS_END);
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

                        response = new Message(stringBuilder.toString());
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
     * Writes a Message object into socket's output stream
     * @param message HTTP message
     */
    public void writeMessage(Message message) {
        printWriter.write(message.toString());
        printWriter.flush();
        if (message.getBody() != null){
            try {
                socket.getOutputStream().write(message.getBody());
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
