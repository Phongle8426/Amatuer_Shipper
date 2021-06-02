package com.example.AmateurShipper.Util;

import java.text.NumberFormat;
import java.util.Locale;

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

    public String formatMoney(long money){
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(money);
        return str1;
    }
}
