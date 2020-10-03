package client;

import data.Response;

public class Filter {
    public final String replace;
    public final String with;

    public Filter(String replace, String with){
        this.replace = replace;
        this.with = with;
    }

    public void filter(Response response){
        if (response.getHeaderValue("Content-Type").contains("text")){
            response.setBody(new String(response.getBody()).replaceAll(replace, with).getBytes());
        }
//        response.setData(response.getData().replaceAll(replace, with));
    }
}
