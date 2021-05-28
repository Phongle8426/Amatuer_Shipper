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
}
