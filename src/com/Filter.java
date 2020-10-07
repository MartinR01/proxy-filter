package com;

import messages.ResponseMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {
    public static final Pattern HTMLPattern = Pattern.compile(">([\\w\\W]+?)<");
    public final String replace;
    public final String with;
    public final boolean onlyText;

    public Filter(String replace, String with, boolean onlyText){
        this.replace = replace;
        this.with = with;
        this.onlyText = onlyText;
    }

    public void filter(ResponseMessage response){
        if (response == null){
            return;
        }
        if (response.getStatus().contains("200 OK") && response.getContentType().contains("text")){
            String body = new String(response.getBody());
            if(onlyText && response.getContentType().contains("text/html")) {
                Matcher matcher = HTMLPattern.matcher(body);
                StringBuffer stringBuffer = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(stringBuffer, body.substring(matcher.start(), matcher.start(1))
                            + matcher.group(1).replaceAll(replace, with)
                            + body.substring(matcher.end(1), matcher.end()));
                }
                matcher.appendTail(stringBuffer);
                response.setBody(stringBuffer.toString().getBytes());
            } else {
                response.setBody(body.replaceAll(replace, with).getBytes());
            }
        }
    }
}
