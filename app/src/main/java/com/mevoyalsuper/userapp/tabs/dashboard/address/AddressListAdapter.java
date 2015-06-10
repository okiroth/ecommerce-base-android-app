package com.mevoyalsuper.userapp.tabs.dashboard.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.Address;

/**
 * Created by ivan on 5/27/15.
 */
public class AddressListAdapter extends ArrayAdapter{

    ViewHolder viewHolder; // view lookup cache stored in tag
    Context context;

    // Init always with an empty list, use callbacks to update
    public AddressListAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Address address = (Address) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sinlge_address_listview, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.street.setText(address.street);
        viewHolder.city.setText(address.city);

        return convertView;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public TextView street;
        public TextView city;

        public ViewHolder(View view) {
            street = (TextView) view.findViewById(R.id.address_street);
            city = (TextView) view.findViewById(R.id.address_city);
        }
    }
}
