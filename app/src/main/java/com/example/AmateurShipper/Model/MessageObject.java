package com.example.AmateurShipper.Model;

public class MessageObject {
    String  message, id,imgmessage,isseen,timemessage,name;

    public MessageObject() {
    }

    public MessageObject( String message, String id,String imgmessage,String isseen,String timemessage,String name) {
        this.message = message;
        this.id = id;
        this.imgmessage = imgmessage;
        this.isseen = isseen;
        this.timemessage = timemessage;
        this.name = name;
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

    public String getIsseen() {
        return isseen;
    }

    public void setIsseen(String isseen) {
        this.isseen = isseen;
    }

    public String getTimemessage() {
        return timemessage;
    }

    public void setTimemessage(String timemessage) {
        this.timemessage = timemessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
