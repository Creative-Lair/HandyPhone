package com.creativelair.handyphone.Helpers;

/**
 * Created by AHSAN on 4/9/2017.
 */

public class Message {
    private String header;
    private String text;

    public Message(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public Message(){

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
