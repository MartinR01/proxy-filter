import client.ClientConnection;
import client.ServerClientMediator;
import com.sun.org.apache.regexp.internal.RE;
import data.Request;
import server.Server;

import java.io.IOException;

public class Main{
    public static void main(String[] args){
//        Server server = Server.getInstance();
//
//        while(true){
//            server.acceptConnection();
//        }
        try {
            ClientConnection c = new ClientConnection("zebroid.ida.liu.se", null);
            c.receiveMessage(new Request("GET http://zebroid.ida.liu.se/fakenews/test1.txt HTTP/1.1\r\n" +
                    "Host: zebroid.ida.liu.se\r\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:81.0) Gecko/20100101 Firefox/81.0\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                    "Accept-Language: en-US,en;q=0.5\r\n" +
                    "Accept-Encoding: gzip, deflate\r\n" +
                    "DNT: 1\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Upgrade-Insecure-Requests: 1\r\n" +
                    "Pragma: no-cache\r\n" +
                    "Cache-Control: no-cache\r\n\r\n"));
//            c = new ClientConnection("zebroid.ida.liu.se", null);
            c.connect();
            c.receiveMessage(new Request("GET http://zebroid.ida.liu.se/fakenews/test1.txt HTTP/1.1\r\n" +
                    "Host: zebroid.ida.liu.se\r\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:81.0) Gecko/20100101 Firefox/81.0\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                    "Accept-Language: en-US,en;q=0.5\r\n" +
                    "Accept-Encoding: gzip, deflate\r\n" +
                    "DNT: 1\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Upgrade-Insecure-Requests: 1\r\n" +
                    "Pragma: no-cache\r\n" +
                    "Cache-Control: no-cache\r\n\r\n"));
//            c = new ClientConnection("zebroid.ida.liu.se", null);
            c.connect();
            c.receiveMessage(new Request("GET http://zebroid.ida.liu.se/favicon.ico HTTP/1.1\r\n" +
                    "Host: zebroid.ida.liu.se\r\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:81.0) Gecko/20100101 Firefox/81.0\r\n" +
                    "Accept: image/webp,*/*\r\n" +
                    "Accept-Language: en-US,en;q=0.5\r\n" +
                    "Accept-Encoding: gzip, deflate\r\n" +
                    "DNT: 1\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Referer: http://zebroid.ida.liu.se/fakenews/test1.html\r\n" +
                    "Pragma: no-cache\r\n" +
                    "Cache-Control: no-cache\r\n" +
                    "\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
