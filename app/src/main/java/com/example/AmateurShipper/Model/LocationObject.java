package com.example.AmateurShipper.Model;

public class LocationObject {

    String longShop,latShop,longUser,latUser;

    public LocationObject() {
    }

    public LocationObject(String longShop, String latShop, String longUser, String latUser) {
        this.latShop = latShop;
        this.longShop = longShop;
        this.latUser = latUser;
        this.longUser = longUser;
    }


    public String getLatShop() {
        return latShop;
    }

    public void setLatShop(String latShop) {
        this.latShop = latShop;
    }

    public String getLongShop() {
        return longShop;
    }

    public void setLongShop(String longShop) {
        this.longShop = longShop;
    }

    public String getLatUser() {
        return latUser;
    }

    public void setLatUser(String latUser) {
        this.latUser = latUser;
    }

    public String getLongUser() {
        return longUser;
    }

    public void setLongUser(String longUser) {
        this.longUser = longUser;
    }
}
