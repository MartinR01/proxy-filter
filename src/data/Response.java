package data;

import java.util.HashMap;

public class Response {
    private String data;

    private String status;
    private HashMap<String, String> headers;
    private byte[] body;

    public Response(String headers){
        System.out.println("---- New Response ----\n'"+headers+"'");
        String[] all = headers.split("\r\n");

        this.headers = new HashMap<>();
        this.status = all[0];

        for (int i = 1; i < all.length; i++){
            if (all[i].contains(": ")){
                String[] keyValue = all[i].split(": ");
                this.headers.put(keyValue[0], keyValue[1]);
            }
        }
    }

    private String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

    public String getHeaderValue(String name){
        // TODO needs case insensitive matching, because HTTP allows for different types
        return headers.get(name);
    }

    public int getContentSize(){
        return Integer.parseInt(getHeaderValue("Content-Length"));
    }

    public void setBody(byte[] body){
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(status + "\r\n");
        for (String key : headers.keySet()){
            stringBuilder.append(key + ": "+ headers.get(key) + "\r\n");
        }
        stringBuilder.append("\r\n");
        // TODO make writable
//        stringBuilder.append(Arrays.toString(body));

        return stringBuilder.toString();
    }
}
