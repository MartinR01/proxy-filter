package processing;

import client.Client;
import server.Server;

// TODO
public class Pipeline {
    private Server server;
    private Client client;

    public Pipeline(){
        this.server = Server.getInstance();
    }
}
