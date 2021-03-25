package com.example.AmateurShipper.Model;

public class ProfileObject {
    String name,phone,address,email,avatar,cmnd;

    public ProfileObject() {
    }

    public ProfileObject(String name, String phone, String address, String email, String avatar, String cmnd) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
        this.cmnd = cmnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }
}
