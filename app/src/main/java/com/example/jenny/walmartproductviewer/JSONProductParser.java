package com.example.jenny.walmartproductviewer;

import android.graphics.Bitmap;

import com.example.jenny.walmartproductviewer.model.Category;
import com.example.jenny.walmartproductviewer.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.jenny.walmartproductviewer.MainActivity.whc;

public class JSONProductParser {

    public static ArrayList<Category> getCategories(String data) throws JSONException {
        JSONObject jObj = new JSONObject(data);
        ArrayList<Category> categories = new ArrayList<>();
        //create JSONArray for the list of categories
        JSONArray jArr = jObj.getJSONArray("categories");
        Category category;
        //iterate through the JSONArray and create a new Category Object for each
        for(int i = 0; i < jArr.length(); i++) {
            category = new Category();
            //populate new category obj with info
            JSONObject cat = jArr.getJSONObject(i);
            category.setId(getString("id", cat));
            category.setName(getString("name", cat));
            categories.add(category);
        }
        return categories;

    }

    public static ArrayList<Product> getProducts(String data) throws JSONException{
        JSONObject jObj = new JSONObject(data);
        //create JSONArray for the list of products
        ArrayList<Product> products = new ArrayList<>();
        JSONArray jArr = jObj.getJSONArray("items");
        Product product;
        //iterate through the JSONArray and create a new Product Object for each
        for(int i = 0; i < jArr.length(); i++) {
            product = new Product();
            //populate new product obj with info
            JSONObject prod = jArr.getJSONObject(i);
            product.setId(getInt("itemId", prod));
            product.setName(getString("name", prod));
            product.setSalePrice(getFloat("salePrice", prod));
            product.setImage(getString("largeImage", prod));
            //check if there is an image for the product first
            if(product.getImage() != null) {
                Bitmap img = (whc.getImage(product.getImage()));
                product.setImageData(img);
            }
            products.add(product);
        }
        return products;

    }

    public static Product getProductDetails(String data) throws  JSONException{
        JSONObject prod = new JSONObject(data);
        Product product = new Product();
        //populate new product obj with info
        product.setId(getInt("itemId", prod));
        product.setName(getString("name", prod));
        product.setSalePrice(getFloat("salePrice", prod));
        product.setImage(getString("largeImage", prod));
        //check if there is an image for the product first
        if(product.getImage() != null) {
            Bitmap img = (whc.getImage(product.getImage()));
            product.setImageData(img);
        }
        product.setBrand(getString("brandName", prod));
        product.setDesc(getString("longDescription", prod));
        product.setStock(getString("stock", prod));
        product.setProdURL(getString("productUrl", prod));

        return product;
    }

    public static String getNextPage(String data) throws JSONException {
        JSONObject jObj = new JSONObject(data);
        //parse and get the URL for the next page
        String nextPageURL = getString("nextPage", jObj);
        return nextPageURL;
    }

    //helper functions//
    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        if(jObj.has(tagName)) {
            return jObj.getString(tagName);
        } else {
            return null;
        }
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        if(jObj.has(tagName)){
            return (float) jObj.getDouble(tagName);
        } else {
            return -1;
        }
    }

    private static int getInt(String tagname, JSONObject jObj) throws JSONException {
        if(jObj.has(tagname)) {
            return jObj.getInt(tagname);
        } else {
            return -1;
        }
    }
}
