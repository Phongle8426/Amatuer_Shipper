package com.example.AmateurShipper;

public class MessageObject {
    String  message, id;

    public MessageObject() {
    }

    public MessageObject( String message, String id) {

        this.message = message;
        this.id = id;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
