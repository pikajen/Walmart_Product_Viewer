package com.example.jenny.walmartproductviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jenny.walmartproductviewer.model.Product;

import org.json.JSONException;

public class ProductActivity extends Activity {
    private TextView productDetailName;
    private ImageView productDetailImage;
    private TextView productDetailPrice;
    private TextView productDetailStock;
    private TextView productDetailDesc;
    private TextView productDetailBrand;
    private TextView productDetailURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_layout);
        Intent intent = getIntent();
        //retrieve the product id stored in intent
        String productUrl = intent.getDataString();
        productDetailName = findViewById(R.id.prodDetailName);
        productDetailDesc = findViewById(R.id.prodDetailDescr);
        productDetailBrand = findViewById(R.id.prodDetailBrand);
        productDetailPrice = findViewById(R.id.prodDetailPrice);
        productDetailStock = findViewById(R.id.prodDetailStock);
        productDetailURL = findViewById(R.id.prodDetailURL);
        productDetailImage = findViewById(R.id.prodDetailImage);
        //start getting details of the product
        new JSONProductDetailTask().execute(productUrl);
    }

    public class JSONProductDetailTask extends AsyncTask<String, Void, Product> {

        @Override
        protected Product doInBackground(String... params) {
            Product product = new Product();
            String data = ((new WalmartHttpClient().getProductDetailData(params[0])));
            try {
                //parse through the data to get a Product with details populated
                product = JSONProductParser.getProductDetails(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return product;
        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            if(product.getImageData() != null) {
                productDetailImage.setImageBitmap(product.getImageData());
            }
            //removing html tags in string
            String html = Html.fromHtml(product.getDesc()).toString();
            productDetailDesc.setText(Html.fromHtml(html));
            productDetailName.setText(product.getName());
            productDetailBrand.setText("Brand: " + product.getBrand());
            productDetailStock.setText("Stock: "+ product.getStock());
            productDetailURL.setText(product.getProdURL());
            if(product.getSalePrice() != -1) {
                productDetailPrice.setText("Price: $" + Float.toString(product.getSalePrice()));
            } else {
                productDetailPrice.setText("Price: N/A");
            }
        }


    }

}
