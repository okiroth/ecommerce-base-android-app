package com.mevoyalsuper.userapp.tabs.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mevoyalsuper.userapp.models.Product;
import com.squareup.picasso.Picasso;
import com.mevoyalsuper.userapp.R;

/**
 * Created by ivan on 5/27/15.
 */
public class ItemsGridAdapter extends ArrayAdapter{

    ViewHolder viewHolder; // view lookup cache stored in tag
    Context context;

    // Init always with an empty list, use callbacks to update
    public ItemsGridAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = (Product) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sinlge_item_gridview, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.price.setText("$" + product.price);
        viewHolder.name.setText(product.name);
        viewHolder.inCart.setText(product.getCartAmount());

        Picasso.with(context)
                .load(product.img)
                .into(viewHolder.img);

        return convertView;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public ImageView img;
        public TextView price;
        public TextView  name;
        public TextView inCart;

        public ViewHolder(View view) {
            img = (ImageView) view.findViewById(R.id.list_item_img);
            price = (TextView) view.findViewById(R.id.list_item_price);
            name = (TextView) view.findViewById(R.id.list_item_name);
            inCart = (TextView) view.findViewById(R.id.grid_incart_amount);
        }
    }
}
