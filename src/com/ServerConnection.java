package com;

import messages.AMessage;
import messages.RequestMessage;
import messages.ResponseMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * The class ServerConnection is socket wrapper for serving clients
 */
public class ServerConnection extends AConnection{
    private final ServerClientMediator mediator;

    /**
     * Constructs new instance on a socket created by incoming connection to the Server
     * @param socket socket object, typically created by incoming connection to the Server
     * @throws IOException in case IO streams of the socket cannot be opened
     */
    public ServerConnection(Socket socket) throws IOException{
        super(socket);
        this.mediator = new ServerClientMediator(this);
    }

    /**
     * Reads an incoming request and sends it to the mediator
     */
    public void run(){
        try {
            AMessage request = readResponse();
            if(request == null){
                return;
            }
            mediator.messageClient(new RequestMessage(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes received response to socket's output, then starts reading another request
     * @param response received response object
     */
    public void receiveMessage(ResponseMessage response){
        writeRequest(response.getMessage());
        run();
    }

}
