import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main{
    public static void main(String[] args){
        final int PORT = 8080;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("creatig streams....");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true) {
                    System.out.println("reading message....");
                    String s = bufferedReader.readLine();
                    String msg = "";

                    while (!s.equals("")){
                        msg += s + "\n";
                        s = bufferedReader.readLine();
                    }

                    System.out.println(msg);
                    String host = msg.split("\n")[1].split(" ")[1];
                    System.out.println(host);

                    Socket clientSocket = new Socket(host, 80);
                    BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    System.out.println("Created client socket");

                    msg = msg.replace("\n", "\r\n") + "\r\n";
                    System.out.println("masg: "+ msg);

                    clientWriter.write(msg);
                    clientWriter.flush();
                    System.out.println("sent message to the server");

                    String r = clientReader.readLine();
                    String response = "";

                    while (r != null){
                        response += r + "\n";
                        r = clientReader.readLine();
                    }
                    System.out.println("Read the response");

                    System.out.println(response);
                    System.out.println("-----------");

                    bufferedWriter.write(response);
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
