package com.example.jenny.walmartproductviewer.model;

import android.graphics.Bitmap;

public class Product {
    private int id;
    private String name;
    private float salePrice;
    private  String image;

    public Bitmap imageData;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageData(Bitmap imageData) {
        this.imageData = imageData;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public String getImage() {
        return image;
    }

    public Bitmap getImageData() {
        return imageData;
    }
}
