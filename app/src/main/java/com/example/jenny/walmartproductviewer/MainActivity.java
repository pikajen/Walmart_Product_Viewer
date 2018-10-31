package com.example.jenny.walmartproductviewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jenny.walmartproductviewer.model.Category;
import com.example.jenny.walmartproductviewer.model.Product;

import org.json.JSONException;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.example.jenny.walmartproductviewer.WalmartHttpClient.API_KEY;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.BASE_URL;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.CATID_URL;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.COUNT_URL;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.PROD_URL;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.QUERY_URL;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.SEARCH_URL;
import static com.example.jenny.walmartproductviewer.WalmartHttpClient.V_URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lv;
    private GridView gv;
    private ListViewCustomAdapter adapter;
    private ArrayList<Object> categoryList;
    private ArrayList<Object> productList;
    private TextView nextButton;
    private TextView prevButton;
    private String nextPageString;
    private String currentPageString;
    private Stack<String> prevPages;
    public static WalmartHttpClient whc;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whc = new WalmartHttpClient();
        lv = (ListView) findViewById(R.id.categoriesList);
        gv = (GridView) findViewById(R.id.productGrid);
        nextButton = (TextView) findViewById(R.id.button2);
        prevButton = (TextView) findViewById(R.id.prevButton);
        editText = (EditText) findViewById(R.id.etValue);
        lv.setOnItemClickListener(this);
        gv.setOnItemClickListener(this);
        categoryList = new ArrayList<Object>();
        productList = new ArrayList<>();
        prevPages = new Stack<>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        JSONWalmartTask task = new JSONWalmartTask();
        task.execute();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //hide soft keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    //get the input
                    String text = v.getText().toString();
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    String url = BASE_URL + V_URL + SEARCH_URL + "&" + QUERY_URL + text + API_KEY;
                    lv.setVisibility(View.GONE);
                    gv.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    editText.getText().clear();
                    prevPages.clear();
                    new JSONWalmartProdTask().execute(url);
                    return true;
                }
                return false;
            }
        });

    }



    @Override
    public void onBackPressed() {
      //  if(!isProductView) {
            lv.setVisibility(View.VISIBLE);
            gv.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            adapter = null;
            gv.setAdapter(adapter);
            adapter = new ListViewCustomAdapter(MainActivity.this, categoryList, 1);
            lv.setAdapter(adapter);
     //   } else {

//            setContentView(R.layout.activity_main);
//            gv = (GridView) findViewById(R.id.productGrid);
//            gv.setOnItemClickListener(this);
//            nextButton = (TextView) findViewById(R.id.button2);
//            prevButton = (TextView) findViewById(R.id.prevButton);
//            isProductView = false;
//            gv.setVisibility(View.VISIBLE);
//            nextButton.setVisibility(View.VISIBLE);
//            adapter = new ListViewCustomAdapter(MainActivity.this, productList, 2);
//            gv.setAdapter(adapter);
            //String prevPage = prevPages.pop();
            //new JSONWalmartProdTask().execute(currentPageString);
//        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(adapter.type == 1) {
            lv.setVisibility(View.GONE);
            gv.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            Category category = (Category) adapter.getItem(position);
            Toast.makeText(this, category.getName(), Toast.LENGTH_SHORT).show();
            String url = BASE_URL + V_URL + PROD_URL + CATID_URL + category.getId() + "&" + COUNT_URL + API_KEY;
            new JSONWalmartProdTask().execute(url);
        } else if (adapter.type == 2) {
            Intent itemIntent = new Intent(this, ProductActivity.class);
            String url = Long.toString(adapter.getItemId(position));
            itemIntent.setData(Uri.parse(url));
            startActivity(itemIntent);

        }
    }

    public void onNextPage(View view) {
        if(prevButton.getVisibility() == View.GONE) {
            prevButton.setVisibility(View.VISIBLE);
        }
        prevPages.push(currentPageString);
        Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
        new JSONWalmartProdTask().execute(BASE_URL + nextPageString );
    }

    public void onPrevPage(View view) {
        Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
        if(nextButton.getVisibility() == View.GONE) {
            nextButton.setVisibility(View.VISIBLE);
        }
        String prevPage = prevPages.pop();
        new JSONWalmartProdTask().execute(prevPage);
    }

    private class JSONWalmartTask extends AsyncTask<Void, Void, ArrayList<Category>> {

        @Override
        protected ArrayList<Category> doInBackground(Void... voids) {
            ArrayList<Category> categories = new ArrayList<Category>();
            String data = ((whc.getCategoriesData()));
            try {
                categories = JSONProductParser.getCategories(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return categories;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            super.onPostExecute(categories);

            categoryList.addAll(categories);
            adapter = new ListViewCustomAdapter(MainActivity.this, categoryList, 1);
            lv.setAdapter(adapter);

        }
    }

    private class JSONWalmartProdTask extends AsyncTask<String, Void, ArrayList<Product>> {

        @Override
        protected ArrayList<Product> doInBackground(String...params) {
            ArrayList<Product> products = new ArrayList<Product>();

            String data = ((whc.getProductsData(params[0])));
            try {
                products = JSONProductParser.getProducts(data);
                currentPageString = params[0];
                nextPageString = JSONProductParser.getNextPage(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return products;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            if(nextPageString == null) {
                nextButton.setVisibility(View.GONE);
            }
            if(prevPages.empty()) {
                prevButton.setVisibility(View.GONE);
            }
            productList.clear();
            productList.addAll(products);
            adapter = new ListViewCustomAdapter(MainActivity.this, productList, 2);
            gv.setAdapter(adapter);
        }
    }
}
