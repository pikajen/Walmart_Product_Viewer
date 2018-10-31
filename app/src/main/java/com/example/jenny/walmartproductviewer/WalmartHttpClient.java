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
    public static String API_KEY = "&apiKey=yy5scaux4z2y2y7rs2hz7kw3&format=json";
    public static String BASE_URL = "http://api.walmartlabs.com/";
    public static String V_URL = "v1/";
    public static String CAT_URL = "taxonomy?";
    public static String PROD_URL = "paginated/items?";
    public static String COUNT_URL = "count=10";
    public static String CATID_URL = "category=";

    public String getCategoriesData() {
        return readData(BASE_URL + V_URL + CAT_URL + API_KEY);
    }

    public String getNextProductsData(String nextPageUrl) {
        return readData(nextPageUrl);
    }

    private String readData(String urlString) {
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = (HttpURLConnection) (new URL(urlString)).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.connect();

            int status = connection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();

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

            if (status == HttpURLConnection.HTTP_OK) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
