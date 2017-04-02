package com.hoangsong.zumechat.models;

/**
 * Created by Tang on 3/26/2017.
 */

public class CreditPackageInfo {
    private int id;
    private String name;
    private int day;
    private float price;

    public CreditPackageInfo(int id, String name, int day, float price) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
