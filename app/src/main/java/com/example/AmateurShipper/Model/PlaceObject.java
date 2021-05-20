package com.example.AmateurShipper.Model;

public class PlaceObject {
    double latitude,longitude;
    int type,check,diem_thu;

    public PlaceObject() {
    }

    public PlaceObject(double latitude, double longitude, int type, int check,int diem_thu) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.check = check;
        this.diem_thu = diem_thu;
    }

    public int getDiem_thu() {
        return diem_thu;
    }

    public void setDiem_thu(int diem_thu) {
        this.diem_thu = diem_thu;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
