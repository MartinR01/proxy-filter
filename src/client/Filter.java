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
        response.setData(response.getData().replaceAll(replace, with));
    }
}
