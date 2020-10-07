package com;

import messages.Message;
import messages.ResponseMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents one configuration of string replacements in Messages.
 */
public class Filter {
    /** Matches everything in between HTML tags (the tags don't need to match) */
    public static final Pattern HTMLPattern = Pattern.compile(">([\\w\\W]+?)<");
    /** String to find in sequences */
    public final String replace;
    /** String that will replace found subsequences */
    public final String with;
    /** Specifies if the filter should only consider text in between HTML tags or everything */
    public final boolean onlyText;

    /**
     * Constructs the Filter
     * @param replace String to find in sequences
     * @param with String that will replace found subsequences
     * @param onlyText Specifies if the filter should only consider text in between HTML tags or everything
     */
    public Filter(String replace, String with, boolean onlyText){
        this.replace = replace;
        this.with = with;
        this.onlyText = onlyText;
    }

    /**
     * Modifies the message body by replacing replace string by with strings.
     * Depending on the onlyText, the search will or will not be restricted to in between HTML tags.
     * @param message
     */
    public void filter(Message message){
        if (message == null){
            return;
        }
        ResponseMessage response = new ResponseMessage(message);
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
