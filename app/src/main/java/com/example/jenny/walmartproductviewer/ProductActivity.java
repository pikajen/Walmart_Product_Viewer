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
        String productUrl = intent.getDataString();
        productDetailName = (TextView) findViewById(R.id.prodDetailName);
        productDetailDesc = (TextView) findViewById(R.id.prodDetailDescr);
        productDetailBrand = (TextView) findViewById(R.id.prodDetailBrand);
        productDetailPrice = (TextView) findViewById(R.id.prodDetailPrice);
        productDetailStock = (TextView) findViewById(R.id.prodDetailStock);
        productDetailURL = (TextView) findViewById(R.id.prodDetailURL);
        //productDetailURL.setMovementMethod(LinkMovementMethod.getInstance());
        productDetailImage = (ImageView) findViewById(R.id.prodDetailImage);
        new JSONProductDetailTask().execute(productUrl);
    }

    public class JSONProductDetailTask extends AsyncTask<String, Void, Product> {

        @Override
        protected Product doInBackground(String... params) {
            Product product = new Product();

            String data = ((new WalmartHttpClient().getProductDetailData(params[0])));
            try {
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
            productDetailName.setText(product.getName());
            productDetailBrand.setText("Brand: " + product.getBrand());
            String html = Html.fromHtml(product.getDesc()).toString();
            productDetailDesc.setText(Html.fromHtml(html));
            productDetailStock.setText("Stock: "+ product.getStock());
            productDetailURL.setText(product.getProdURL());
            productDetailPrice.setText("Price: $" + Float.toString(product.getSalePrice()));
        }


    }

}
