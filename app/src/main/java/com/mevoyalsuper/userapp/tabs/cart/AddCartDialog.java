package com.mevoyalsuper.userapp.tabs.cart;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mevoyalsuper.userapp.models.Product;
import com.squareup.picasso.Picasso;
import com.mevoyalsuper.userapp.R;

/**
 * Created by ivan on 6/3/15.
 */
public class AddCartDialog extends DialogFragment {

    public static final int REQUEST_CODE = 1;

    private Product product;
    private View view;


    public static AddCartDialog newInstance(Product product) {
        AddCartDialog frag = new AddCartDialog();
        frag.product = product;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout = product.isInCart() ? R.layout.modify_cart_item_dialog : R.layout.dialog_add_to_cart;
        view = inflater.inflate(layout, container);

        if(product == null) return view;

        TextView name = (TextView) view.findViewById(R.id.item_detail_name);
        name.setText(product.name);

        TextView price = (TextView) view.findViewById(R.id.item_detail_price);
        price.setText("$" + product.price);

        ImageView img = (ImageView) view.findViewById(R.id.item_detail_img);

        Picasso.with(getActivity())
                .load(product.img)
                .resize(300, 300)
                .centerInside()
                .into(img);

        Button minus = (Button) view.findViewById(R.id.item_detail_button_minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusItemAmount();
            }
        });

        Button plus = (Button) view.findViewById(R.id.item_detail_button_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusItemAmount();
            }
        });

        Button add = (Button) view.findViewById(R.id.item_detail_button_add_cart);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart();
            }
        });

        if(product.isInCart()){
            TextView amount = (TextView) view.findViewById(R.id.item_detail_amount);
            amount.setText("" + product.cart);

            Button remove = (Button) view.findViewById(R.id.item_detail_button_remove_cart);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItemFromCart();
                }
            });
        }

        return view;
    }


    public void minusItemAmount(){
        TextView amount = getAmountTextView();
        if(getCartAmount(amount) == 1) return;
        amount.setText("" + (getCartAmount(amount) - 1));
    }

    public void plusItemAmount(){
        TextView amountTextView = getAmountTextView();
        amountTextView.setText("" + (getCartAmount(amountTextView) + 1));
    }

    private int getCartAmount(TextView amount){
        return Integer.parseInt(amount.getText().toString());
    }

    private void addItemToCart(){
        product.cart = getCartAmount(getAmountTextView());
        product.save();
        exit();
    }

    private void removeItemFromCart(){
        product.cart = 0;
        product.save();
        exit();
    }

    private void exit(){
        CartListFragment.REFRESH_CART_LIST = true;
        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, getActivity().getIntent());
        getDialog().dismiss();
    }

    private TextView getAmountTextView(){
        return (TextView) view.findViewById(R.id.item_detail_amount);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // TODO close only with close button?
//        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


}
