import conn.Server;

public class Main{
    public static void main(String[] args){
        Server server = Server.getInstance();
        System.out.println("Running...");

        while(true){
            server.acceptConnection();
        }
    }
}
