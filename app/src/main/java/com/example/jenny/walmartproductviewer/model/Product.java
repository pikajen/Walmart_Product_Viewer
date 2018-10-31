package com.example.jenny.walmartproductviewer.model;

import android.graphics.Bitmap;

public class Product {
    private int id;
    private String name;
    private float salePrice;
    private  String image;
    private Bitmap imageData;
    private String desc;
    private String brand;
    private String prodURL;
    private String stock;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProdURL() {
        return prodURL;
    }

    public void setProdURL(String prodURL) {
        this.prodURL = prodURL;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
