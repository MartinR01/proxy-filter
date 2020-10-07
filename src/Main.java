import com.Server;

public class Main{
    public static void main(String[] args){
        Server server = Server.getInstance();

        while(true){
            server.acceptConnection();
        }
    }
}
