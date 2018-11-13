package com.example.jenny.walmartproductviewer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jenny.walmartproductviewer.model.Category;
import com.example.jenny.walmartproductviewer.model.Product;

import java.util.ArrayList;

import static com.example.jenny.walmartproductviewer.MainActivity.TYPE_CATEGORY;
import static com.example.jenny.walmartproductviewer.MainActivity.TYPE_PRODUCT;

public class ListViewCustomAdapter extends BaseAdapter {
    ArrayList<Object> itemList;

    public Activity context;
    public LayoutInflater inflater;
    public int type;

    public ListViewCustomAdapter(Activity context,ArrayList<Object> itemList, int type) {
        super();
        this.context = context;
        this.itemList = itemList;
        this.type = type;

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        int type = getType();
        if (type == TYPE_CATEGORY) {
            Category item = (Category) getItem(position);
            return Integer.parseInt(item.getId());
        } else if(type == TYPE_PRODUCT) {
            Product item = (Product) getItem(position);
            return item.getId();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public int getType() {
        return type;
    }

    public void setItemList(ArrayList<Object> itemList){
        this.itemList = itemList;
    }

    public void setType(int type) {
        this.type = type;
    }

    //ViewHolder for Category items
    public static class ViewHolderCat{
        TextView categoryName;
    }

    //ViewHolder for Product items
    public static class ViewHolderProd {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        int viewType = getType();
        //get view based on which type of item
        switch (viewType) {
            case TYPE_CATEGORY:
                ViewHolderCat holder;
                if (view == null) {
                    //display category layout
                    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.category_layout, parent, false);
                    holder = new ViewHolderCat();
                    holder.categoryName = view.findViewById(R.id.categoryRow);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolderCat) view.getTag();
                }
                //get selected category item based on position
                Category itemC = (Category) itemList.get(position);
                //if item is not null then populate textview with value
                if (itemC != null) {
                    //set the text to the name of the item stored in selected object
                    holder.categoryName.setText(itemC.getName());
                }
                return view;

            case TYPE_PRODUCT:
                ViewHolderProd holderProd;
                if (view == null) {
                    //display product layout
                    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.product_layout, parent, false);
                    holderProd = new ViewHolderProd();
                    holderProd.productImage = view.findViewById(R.id.productImageView);
                    holderProd.productName = view.findViewById(R.id.productNameTV);
                    holderProd.productPrice = view.findViewById(R.id.productPriceTV);
                    view.setTag(holderProd);
                } else {
                    holderProd = (ViewHolderProd) view.getTag();
                }
                //get selected category itemC based on position
                Product itemP = (Product) itemList.get(position);
                //if item is not null then populate textview with value
                if (itemP != null) {
                    //set the text to the name of the item stored in selected object
                    holderProd.productName.setText(itemP.getName());
                    if(itemP.getImageData() != null) {
                        //setting the bitmap image in the image view
                        holderProd.productImage.setImageBitmap(itemP.getImageData());
                    }
                    //if salePrice is not available (-1) then set the text as N/A
                    if(itemP.getSalePrice() != -1) {
                        holderProd.productPrice.setText("Price: $" + Float.toString(itemP.getSalePrice()));
                    } else {
                        holderProd.productPrice.setText("Price: N/A");
                    }
                }
                return view;
            default:
        }
        return null;
    }

}
