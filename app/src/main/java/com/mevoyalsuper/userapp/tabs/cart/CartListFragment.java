package com.mevoyalsuper.userapp.tabs.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.Product;

import java.util.List;

/**
 * Created by ivan on 5/27/15.
 */
public class CartListFragment extends Fragment {

    public ItemsListAdapter adapter;

    public ListView listView;
    public View rootView;

    public static boolean REFRESH_CART_LIST = false;

    public static CartListFragment newInstance() {
        return new CartListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart_list, container, false);

        adapter = new ItemsListAdapter(getActivity());

        listView = (ListView) rootView.findViewById(R.id.cart_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Product product = (Product) adapter.getItem(position);
                showDialog(product);
            }
        });

        final CartListFragment _this = this;
        rootView.findViewById(R.id.cart_place_order_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersHandler handler = new OrdersHandler(_this);
                handler.placeNewOrder();
            }
        });

        return rootView;
    }

    private void showDialog(Product product) {
        DialogFragment dialogFragment = AddCartDialog.newInstance(product);
        dialogFragment.setTargetFragment(this, AddCartDialog.REQUEST_CODE);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshCartView();
    }

    @Nullable
    @Override
    public View getView() {
        if(REFRESH_CART_LIST){
            refreshCartView();
        }
        return super.getView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCartView();
    }

    /**
     * Refresh the items list in the adapter
     * and calculates the total and show it
     */
    public void refreshCartView(){
        REFRESH_CART_LIST = false;

        List<Product> products = Product.getCartItems();

        double totalNumber = 0;

        for(Product product : products){
            totalNumber += (product.price * product.cart);
        }

        adapter.clear();
        adapter.addAll(products);
        adapter.notifyDataSetChanged();

        TextView total = (TextView) rootView.findViewById(R.id.cart_total_textview);
        total.setText("$" + String.format("%.2f", totalNumber));

        if(totalNumber == 0){
            getView().findViewById(R.id.cart_is_empty_legend).setVisibility(View.VISIBLE);
        }else{
            getView().findViewById(R.id.cart_is_empty_legend).setVisibility(View.GONE);
        }
    }
}


