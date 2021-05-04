package com.example.AmateurShipper.Model;

public class DataStatisticObject {
    String date;
    String amount;

    public DataStatisticObject() {
    }

    public DataStatisticObject(String date, String amount) {

        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
