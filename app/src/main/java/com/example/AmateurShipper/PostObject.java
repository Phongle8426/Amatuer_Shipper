package com.example.AmateurShipper;


public class PostObject {
    String name_poster,time,start_post,end_post,distance,note;
    int imgage_poster,attach_image,quantity_order;
    double fee,payment;

    public PostObject() {
    }

    public PostObject(String name_poster, String time, String start_post, String end_post, String distance, String note, int imgage_poster, int attach_image, int quantity_order, double fee, double payment) {
        this.name_poster = name_poster;
        this.time = time;
        this.start_post = start_post;
        this.end_post = end_post;
        this.distance = distance;
        this.note = note;
        this.imgage_poster = imgage_poster;
        this.attach_image = attach_image;
        this.quantity_order = quantity_order;
        this.fee = fee;
        this.payment = payment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAttach_image() {
        return attach_image;
    }

    public void setAttach_image(int attach_image) {
        this.attach_image = attach_image;
    }

    public String getName_poster() {
        return name_poster;
    }

    public void setName_poster(String name_poster) {
        this.name_poster = name_poster;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStart_post() {
        return start_post;
    }

    public void setStart_post(String start_post) {
        this.start_post = start_post;
    }

    public String getEnd_post() {
        return end_post;
    }

    public void setEnd_post(String end_post) {
        this.end_post = end_post;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getImgage_poster() {
        return imgage_poster;
    }

    public void setImgage_poster(int imgage_poster) {
        this.imgage_poster = imgage_poster;
    }

    public int getQuantity_order() {
        return quantity_order;
    }

    public void setQuantity_order(int quantity_order) {
        this.quantity_order = quantity_order;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }
}
