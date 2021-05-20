package com.example.AmateurShipper.Model;

public class NotificationWebObject {

    String id_post,id_ship,status,thoi_gian;

    public NotificationWebObject() {
    }

    public NotificationWebObject(String id_post, String id_ship, String status, String thoi_gian) {
        this.id_post = id_post;
        this.id_ship = id_ship;
        this.status = status;
        this.thoi_gian = thoi_gian;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getId_ship() {
        return id_ship;
    }

    public void setId_ship(String id_ship) {
        this.id_ship = id_ship;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThoi_gian() {
        return thoi_gian;
    }

    public void setThoi_gian(String thoi_gian) {
        this.thoi_gian = thoi_gian;
    }
}
