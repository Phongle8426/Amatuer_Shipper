package com.example.AmateurShipper.Model;

public class ReportObject {

    String content,email,fullName,id_post,id_report,status,time,type,by,admin;

    public ReportObject() {
    }

    public ReportObject(String content, String email, String fullName, String id_post, String id_report,
                        String status, String time, String type, String by, String admin) {
        this.content = content;
        this.email = email;
        this.fullName = fullName;
        this.id_post = id_post;
        this.id_report = id_report;
        this.status = status;
        this.time = time;
        this.type = type;
        this.by = by;
        this.admin = admin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getId_report() {
        return id_report;
    }

    public void setId_report(String id_report) {
        this.id_report = id_report;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
