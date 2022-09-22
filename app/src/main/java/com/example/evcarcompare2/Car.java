package com.example.evcarcompare2;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.lang.reflect.Array;

public class Car implements Serializable {

    String name, model, make , disPrice, trim, url;
    private transient Bitmap bitmap;
    Boolean isFavorited;
    int price, year, horsepower, range, seats;
    String[] trimList;
    public Car(String n, String ma, String mo, int y, int p, String dp, String[]t, String u, int h, int r, int s){
        name = n;
        model = mo;
        make = ma;
        year = y;
        price = p;
        disPrice = dp;
        isFavorited = false;
        trim = "";
        trimList = t;
        url = u;
        horsepower = h;
        range = r;
        seats = s;
    }

    public String getName() { return name; }

    public String getModel(){
        return model;
    }

    public String getMake(){
        return make;
    }

    public int getYear(){
        return year;
    }

    public int getPrice(){
        return price;
    }

    public String getDisPrice() { return disPrice; }

    public Boolean getFavorited() {
        return isFavorited;
    }

    public void setFavorited(Boolean favorited) {
        isFavorited = favorited;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public String getUrl() {
        return url;
    }

    public String[] getTrimList() { return trimList; }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getHorsepower() { return horsepower; }

    public int getRange() { return range; }

    public int getSeats() { return seats; }
}

