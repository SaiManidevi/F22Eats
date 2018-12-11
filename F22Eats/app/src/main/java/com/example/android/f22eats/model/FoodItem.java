package com.example.android.f22eats.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodItem implements Serializable {

    /**
     * average_rating : 3.5
     * image_url : https://cdn.pixabay.com/photo/2014/09/18/21/17/sandwich-451403_640.jpg
     * item_name : Sandwich
     * item_price : 80
     */

    @SerializedName("average_rating")
    @Expose
    private double average_rating;

    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("item_name")
    @Expose
    private String item_name;

    @SerializedName("item_price")
    @Expose
    private double item_price;

    public FoodItem(double average_rating, String image_url, String item_name, double item_price) {
        this.average_rating = average_rating;
        this.image_url = image_url;
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(double average_rating) {
        this.average_rating = average_rating;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }
}
