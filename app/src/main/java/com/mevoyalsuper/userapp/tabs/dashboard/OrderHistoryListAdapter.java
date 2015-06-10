package com.mevoyalsuper.userapp.tabs.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.OrderShop;
import com.mevoyalsuper.userapp.utils.GlobalValues;

/**
 * Created by ivan on 5/27/15.
 */
public class OrderHistoryListAdapter extends ArrayAdapter{

    ViewHolder viewHolder;
    Context context;

    public OrderHistoryListAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderShop order = (OrderShop) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sinlge_order_listview, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // TODO check all status

        String status = order.status == GlobalValues.STATUS_DELIVER_DONE ? "Entregado" : order.delivery_datetime;

        viewHolder.date.setText(order.date);
        viewHolder.delivery.setText(status);
        viewHolder.total.setText("$" + (order.charge_items + order.charge_delivery));

        int icon = R.drawable.ic_check_circle_black_18dp;
        if(order.status != GlobalValues.STATUS_DELIVER_DONE) icon = R.drawable.ic_local_shipping_black_18dp;
        viewHolder.icon.setImageResource(icon);

        return convertView;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public TextView date;
        public TextView delivery;
        public TextView total;
        public ImageView icon;

        public ViewHolder(View view) {
            date = (TextView) view.findViewById(R.id.history_order_date);
            delivery = (TextView) view.findViewById(R.id.history_order_delivery);
            total = (TextView) view.findViewById(R.id.history_order_total);
            icon = (ImageView) view.findViewById(R.id.history_order_icon);
        }
    }
}
