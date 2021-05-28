package com.example.AmateurShipper.Model;

public class ProfileObject {
    String fullname,phone,address,email,avatar,cmnd,rate_star,birthday,id,level,sexual,role;

    public ProfileObject() {
    }

    public ProfileObject(String fullname, String phone, String address, String email,
                         String avatar, String cmnd, String rate_star, String birthday, String id, String level,
                         String sexual, String role) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
        this.cmnd = cmnd;
        this.rate_star = rate_star;
        this.birthday = birthday;
        this.id = id;
        this.level = level;
        this.sexual = sexual;
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getRate_star() {
        return rate_star;
    }

    public void setRate_star(String rate_star) {
        this.rate_star = rate_star;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSexual() {
        return sexual;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
