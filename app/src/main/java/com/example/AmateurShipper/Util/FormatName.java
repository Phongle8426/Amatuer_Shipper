package com.example.AmateurShipper.Util;

public class FormatName {
    public FormatName() {
    }

    public String formatName(String name){
        if (name.length()<=15)
            return name;
        else{
           String subName  = name.substring(13,name.length());
           String name1 = name.replaceAll(subName,"..");
           return name1;
        }
    }
}
