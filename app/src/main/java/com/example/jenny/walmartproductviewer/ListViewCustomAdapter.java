package com.example.jenny.walmartproductviewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jenny.walmartproductviewer.model.Category;
import com.example.jenny.walmartproductviewer.model.Product;

import java.util.ArrayList;

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
        return 0;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return type;
//    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public int getType() {
        return type;
    }

    public static class ViewHolder{
        TextView categoryName;
    }

    public static class ViewHolderProd {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getType();
        switch (viewType) {
            case 1:
                ViewHolder holder;
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.category_layout, parent, false);
                    holder = new ViewHolder();
                    holder.categoryName = (TextView) v.findViewById(R.id.categoryRow);
                    v.setTag(holder);
                } else {
                    holder = (ViewHolder) v.getTag();
                }
                Category item = (Category) itemList.get(position);
                if (item != null) {
                    if (holder.categoryName != null) {
                        holder.categoryName.setText(item.getName());
                    }
                }
                return v;

            case 2:
                ViewHolderProd holderProd;
                View view = convertView;
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.product_layout, parent, false);
                    holderProd = new ViewHolderProd();
                    holderProd.productImage = (ImageView)view.findViewById(R.id.productImageView);
                    holderProd.productName = (TextView) view.findViewById(R.id.productNameTV);
                    holderProd.productPrice = (TextView) view.findViewById(R.id.productPriceTV);
                    view.setTag(holderProd);
                } else {
                    holderProd = (ViewHolderProd) view.getTag();
                }
                Product itemP = (Product) itemList.get(position);
                if (itemP != null) {
                    if (holderProd.productName != null) {
                        if(itemP.getImageData() != null) {
                            holderProd.productImage.setImageBitmap(itemP.getImageData());
                        }
                        holderProd.productName.setText(itemP.getName());
                        if(itemP.getSalePrice() != -1) {
                            holderProd.productPrice.setText("Price: $" + Float.toString(itemP.getSalePrice()));
                        } else {
                            holderProd.productPrice.setText("Price: N/A");
                        }
                    }

                }

                return view;
            default:
        }
        return null;
    }

}
