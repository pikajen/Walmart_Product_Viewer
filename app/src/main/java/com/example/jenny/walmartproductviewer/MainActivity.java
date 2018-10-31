package com.example.jenny.walmartproductviewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jenny.walmartproductviewer.model.Category;
import com.example.jenny.walmartproductviewer.model.Product;

import org.json.JSONException;

import java.util.ArrayList;
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

    //constants
    public final static int TYPE_CATEGORY = 1;
    public final static int TYPE_PRODUCT = 2;

    private ListView lv;
    private GridView gv;
    private ListViewCustomAdapter adapter;
    private TextView nextButton;
    private TextView prevButton;
    private EditText editText;

    private String nextPageString;
    private String currentPageString;
    private ArrayList<Object> categoryList;
    private ArrayList<Object> productList;
    private Stack<String> prevPagesStack;
    public static WalmartHttpClient whc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whc = new WalmartHttpClient();

        lv = findViewById(R.id.categoriesList);
        gv = findViewById(R.id.productGrid);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        editText = findViewById(R.id.searchInput);
        //set listener for each item in the list/grid views
        lv.setOnItemClickListener(this);
        gv.setOnItemClickListener(this);

        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        //create stack to store the history of pages traversed
        prevPagesStack = new Stack<>();

        //do not change layout when keyboard input is displayed
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //start parsing the categories
        JSONWalmartCategoryTask task = new JSONWalmartCategoryTask();
        task.execute();

        //listener for when a user inputs a search
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //if the user presses done on the keyboard
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //show grid view for products
                    lv.setVisibility(View.GONE);
                    gv.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    //clear the stack
                    prevPagesStack.clear();
                    //hide soft keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    //get the input
                    String text = v.getText().toString();
                    //clear the editText box
                    editText.getText().clear();
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    //create the url to get products matching the input
                    String url = BASE_URL + V_URL + SEARCH_URL + "&" + QUERY_URL + text + API_KEY;
                    //start parsing products from the url
                    new JSONWalmartProdTask().execute(url);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        //return to category page and destroy gridView
        lv.setVisibility(View.VISIBLE);
        gv.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        adapter = null;
        gv.setAdapter(adapter);
        adapter = new ListViewCustomAdapter(MainActivity.this, categoryList, TYPE_CATEGORY);
        lv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //check the type of the item clicked
        if(adapter.type == TYPE_CATEGORY) {
            //if item is category then query products based on category selected
            //display grid view
            lv.setVisibility(View.GONE);
            gv.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            //get the category id from the position
            Category category = (Category) adapter.getItem(position);
            Toast.makeText(this, category.getName(), Toast.LENGTH_SHORT).show();
            //create the url to get products under the respective category id
            String url = BASE_URL + V_URL + PROD_URL + CATID_URL + category.getId() + "&" + COUNT_URL + API_KEY;
            new JSONWalmartProdTask().execute(url);

        } else if (adapter.type == TYPE_PRODUCT) {
            //if item is product then start new activity that displays the product details
            Intent itemIntent = new Intent(this, ProductActivity.class);
            //save the itemID in the intent
            String url = Long.toString(adapter.getItemId(position));
            itemIntent.setData(Uri.parse(url));
            startActivity(itemIntent);
        }
    }

    //onClick handlers//
    public void onNextPage(View view) {
        Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
        if(prevButton.getVisibility() == View.GONE) {
            prevButton.setVisibility(View.VISIBLE);
        }
        //push the current page's url string in the stack
        prevPagesStack.push(currentPageString);
        new JSONWalmartProdTask().execute(BASE_URL + nextPageString );
    }

    public void onPrevPage(View view) {
        Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
        if(nextButton.getVisibility() == View.GONE) {
            nextButton.setVisibility(View.VISIBLE);
        }
        //get the most recent previous page from the stack
        String prevPage = prevPagesStack.pop();
        new JSONWalmartProdTask().execute(prevPage);
    }

    //AsyncTasks//
    private class JSONWalmartCategoryTask extends AsyncTask<Void, Void, ArrayList<Category>> {

        @Override
        protected ArrayList<Category> doInBackground(Void... voids) {
            ArrayList<Category> categories = new ArrayList<>();
            String data = ((whc.getCategoriesData()));
            try {
                //parse through the data to get an array of Category objects
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
            //add category views to the list view
            adapter = new ListViewCustomAdapter(MainActivity.this, categoryList, 1);
            lv.setAdapter(adapter);

        }
    }

    private class JSONWalmartProdTask extends AsyncTask<String, Void, ArrayList<Product>> {

        @Override
        protected ArrayList<Product> doInBackground(String...params) {
            ArrayList<Product> products = new ArrayList<>();
            String data = ((whc.getProductsData(params[0])));
            try {
                //parse through the data to get an array of Product objects
                products = JSONProductParser.getProducts(data);
                //store the current page url in a global variable
                currentPageString = params[0];
                //get the next page url and store it
                nextPageString = JSONProductParser.getNextPage(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return products;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            //hide next page button if there is no next page link
            if(nextPageString == null) {
                nextButton.setVisibility(View.GONE);
            }
            //hide previous page button if the stack is empty
            if(prevPagesStack.empty()) {
                prevButton.setVisibility(View.GONE);
            }
            productList.clear();
            productList.addAll(products);
            //add product views to the grid view
            adapter = new ListViewCustomAdapter(MainActivity.this, productList, 2);
            gv.setAdapter(adapter);
        }
    }
}
