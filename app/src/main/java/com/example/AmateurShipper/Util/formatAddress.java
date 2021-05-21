package com.example.AmateurShipper.Util;

public class formatAddress {

    public formatAddress() {
    }
    public String formatAddress(String address){
        String[] strArr = address.split("[,]");
        String street,ward,distreet,city;
        street = strArr[0];
        ward = strArr[1];
        distreet = strArr[2];
        if (ward.contains("Phường"))
        ward = ward.substring(8,ward.length());
        if (distreet.contains("Quận"))
        distreet = distreet.substring(5,distreet.length());
        return street + ", " + ward + ", "+distreet;
    }
}
