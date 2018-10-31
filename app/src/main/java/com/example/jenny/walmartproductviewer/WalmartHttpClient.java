package com.example.jenny.walmartproductviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WalmartHttpClient {
    public final static String API_KEY = "&apiKey=yy5scaux4z2y2y7rs2hz7kw3&format=json";
    public final static String BASE_URL = "http://api.walmartlabs.com/";
    public final static String V_URL = "v1/";
    public final static String CAT_URL = "taxonomy?";
    public final static String PROD_URL = "paginated/items?";
    public final static String COUNT_URL = "count=10";
    public final static String CATID_URL = "category=";
    public final static String PRODLOOKUP_URL = "items/";
    public final static String SEARCH_URL = "search?";
    public final static String QUERY_URL = "query=";

    public String getCategoriesData() {
        //build url to query category data
        return readData(BASE_URL + V_URL + CAT_URL + API_KEY);
    }

    public String getProductsData(String pageUrl) {
        return readData(pageUrl);
    }

    public String getProductDetailData(String prodID){
        //build url for product lookup
        return readData(BASE_URL + V_URL + PRODLOOKUP_URL + prodID + "?" + API_KEY);
    }

    private String readData(String urlString) {
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = (HttpURLConnection) (new URL(urlString)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int status = connection.getResponseCode();
            //check the response
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();
            //parse through the data and store
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            return buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //close the connections
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int status = connection.getResponseCode();
            //check the response
            if (status == HttpURLConnection.HTTP_OK) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection
            if (connection != null) {
                connection.disconnect();
            }
    }
    return null;
}

    public static Bitmap getImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
