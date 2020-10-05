package data;

/**
 * Request object represents one parsed HTTP request
 */
public class Request {
    public final String data;
    public final String host;

    /**
     * Constructs the object and parses the headers.
     * @param data Received HTTP request
     * @throws IllegalArgumentException may be thrown in case the request does not contain
     *      at least one newline character and a space.
     */
    public Request(String data) throws IllegalArgumentException{
        if (!data.contains("\r\n") && data.contains(" ")){
            throw new IllegalArgumentException("Invalid data: '" + data + "'");
        }
        System.out.println("---- New Request ----\n'"+data+"'");
//        System.out.println("Equal: "+data.equals("GET http://zebroid.ida.liu.se/favicon.ico HTTP/1.1\r\n" +
//                "Host: zebroid.ida.liu.se\r\n" +
//                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:81.0) Gecko/20100101 Firefox/81.0\r\n" +
//                "Accept: image/webp,*/*\r\n" +
//                "Accept-Language: en-US,en;q=0.5\r\n" +
//                "Accept-Encoding: gzip, deflate\r\n" +
//                "DNT: 1\r\n" +
//                "Connection: keep-alive\r\n" +
//                "Referer: http://zebroid.ida.liu.se/fakenews/test1.html\r\n" +
//                "Pragma: no-cache\r\n" +
//                "Cache-Control: no-cache\r\n" +
//                "\r\n"));
        this.data = data;
        this.host = data.split("\r\n")[1].split(" ")[1];
    }
}
