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
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            URL url = new URL(BASE_URL + V_URL + CAT_URL + API_KEY);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("accept", "application/json");
//            con.setDoInput(true);
//            con.setDoOutput(true);
            con.connect();

            InputStream inputStream;

            int status = con.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                is = con.getErrorStream();
            else
                is = con.getInputStream();

            StringBuffer buffer = new StringBuffer();
            //is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }

    public String getProductsData(String catID) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            URL url = new URL(BASE_URL + V_URL + PROD_URL + CATID_URL + catID + "&" + COUNT_URL + API_KEY);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("accept", "application/json");
            con.connect();


            int status = con.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                is = con.getErrorStream();
            else
                is = con.getInputStream();

            StringBuffer buffer = new StringBuffer();
            //is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }
    public String getNextProductsData(String nextPageUrl) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            URL url = new URL(nextPageUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("accept", "application/json");
            con.connect();


            int status = con.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                is = con.getErrorStream();
            else
                is = con.getInputStream();

            StringBuffer buffer = new StringBuffer();
            //is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }

    public static Bitmap getImage(URL url) {
    HttpURLConnection connection = null;
    try {
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
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
