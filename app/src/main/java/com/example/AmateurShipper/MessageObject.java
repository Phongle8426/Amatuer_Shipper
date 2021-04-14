package com.example.AmateurShipper;

public class MessageObject {
    String  message, id,imgmessage;

    public MessageObject() {
    }

    public MessageObject( String message, String id,String imgmessage) {
        this.message = message;
        this.id = id;
        this.imgmessage = imgmessage;
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

    public String getImgmessage() {
        return imgmessage;
    }

    public void setImgmessage(String imgmessage) {
        this.imgmessage = imgmessage;
    }
}
