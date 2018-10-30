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
        ArrayList<Category> categories = new ArrayList<Category>();
        JSONArray jArr = jObj.getJSONArray("categories");
        Category category;
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
        ArrayList<Product> products = new ArrayList<Product>();
        JSONArray jArr = jObj.getJSONArray("items");
        Product product;
        for(int i = 0; i < jArr.length(); i++) {
            product = new Product();
            JSONObject prod = jArr.getJSONObject(i);
            product.setId(getInt("itemId", prod));
            product.setName(getString("name", prod));
            product.setSalePrice(getFloat("salePrice", prod));
            product.setImage(getString("largeImage", prod));
            if(product.getImage() != null) {
                Bitmap img = (whc.getImage(product.getImage()));
                product.setImageData(img);
            }
            products.add(product);
        }
        return products;

    }

    public static String getNextPage(String data) throws JSONException {
        JSONObject jObj = new JSONObject(data);
        String nextPageURL = getString("nextPage", jObj);
        return nextPageURL;
    }

    //helper functions
    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

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
