package com.example.AmateurShipper.Model;


public class PostObject {
    public String ten_nguoi_gui;
    String sdt_nguoi_gui;
    public String noi_nhan;
    public String noi_giao;
    public String sdt_nguoi_nhan;
    public String ten_nguoi_nhan;
    public String ghi_chu;
    public String thoi_gian;
    public String id_shop;
    public String phi_giao;
    public String phi_ung;
    public String km;
    public String id_post;
    String status;
    public String receiveLat;
    public String receiveLng;
    public String shipLat;
    public String shipLng;
    public String time_estimate;

    public PostObject() {
    }

    public PostObject(String ten_nguoi_gui, String sdt_nguoi_gui, String noi_nhan, String noi_giao,
                      String sdt_nguoi_nhan, String ten_nguoi_nhan, String ghi_chu, String thoi_gian,
                      String id_shop, String phi_giao, String phi_ung, String km, String id_post, String status,
                      String receiveLat, String receiveLng, String shipLat, String shipLng, String time_estimate) {
        this.ten_nguoi_gui = ten_nguoi_gui;
        this.sdt_nguoi_gui = sdt_nguoi_gui;
        this.noi_nhan = noi_nhan;
        this.noi_giao = noi_giao;
        this.sdt_nguoi_nhan = sdt_nguoi_nhan;
        this.ten_nguoi_nhan = ten_nguoi_nhan;
        this.ghi_chu = ghi_chu;
        this.thoi_gian = thoi_gian;
        this.id_shop = id_shop;
        this.phi_giao = phi_giao;
        this.phi_ung = phi_ung;
        this.km = km;
        this.id_post = id_post;
        this.status = status;
        this.receiveLat = receiveLat;
        this.receiveLng = receiveLng;
        this.shipLat = shipLat;
        this.shipLng = shipLng;
        this.time_estimate = time_estimate;
    }

    public String getReceiveLat() {
        return receiveLat;
    }

    public void setReceiveLat(String receiveLat) {
        this.receiveLat = receiveLat;
    }

    public String getReceiveLng() {
        return receiveLng;
    }

    public void setReceiveLng(String receiveLng) {
        this.receiveLng = receiveLng;
    }

    public String getShipLat() {
        return shipLat;
    }

    public void setShipLat(String shipLat) {
        this.shipLat = shipLat;
    }

    public String getShipLng() {
        return shipLng;
    }

    public void setShipLng(String shipLng) {
        this.shipLng = shipLng;
    }

    public String getTime_estimate() {
        return time_estimate;
    }

    public void setTime_estimate(String time_estimate) {
        this.time_estimate = time_estimate;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getTen_nguoi_gui() {
        return ten_nguoi_gui;
    }

    public void setTen_nguoi_gui(String ten_nguoi_gui) {
        this.ten_nguoi_gui = ten_nguoi_gui;
    }

    public String getSdt_nguoi_gui() {
        return sdt_nguoi_gui;
    }

    public void setSdt_nguoi_gui(String sdt_nguoi_gui) {
        this.sdt_nguoi_gui = sdt_nguoi_gui;
    }

    public String getNoi_nhan() {
        return noi_nhan;
    }

    public void setNoi_nhan(String noi_nhan) {
        this.noi_nhan = noi_nhan;
    }

    public String getNoi_giao() {
        return noi_giao;
    }

    public void setNoi_giao(String noi_giao) {
        this.noi_giao = noi_giao;
    }

    public String getSdt_nguoi_nhan() {
        return sdt_nguoi_nhan;
    }

    public void setSdt_nguoi_nhan(String sdt_nguoi_nhan) {
        this.sdt_nguoi_nhan = sdt_nguoi_nhan;
    }

    public String getTen_nguoi_nhan() {
        return ten_nguoi_nhan;
    }

    public void setTen_nguoi_nhan(String ten_nguoi_nhan) {
        this.ten_nguoi_nhan = ten_nguoi_nhan;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public String getThoi_gian() {
        return thoi_gian;
    }

    public void setThoi_gian(String thoi_gian) {
        this.thoi_gian = thoi_gian;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
    }

    public String getPhi_giao() {
        return phi_giao;
    }

    public void setPhi_giao(String phi_giao) {
        this.phi_giao = phi_giao;
    }

    public String getPhi_ung() {
        return phi_ung;
    }

    public void setPhi_ung(String phi_ung) {
        this.phi_ung = phi_ung;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
