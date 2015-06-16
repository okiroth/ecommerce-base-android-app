package com.mevoyalsuper.userapp.tabs.cart;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.OrderShop;

/**
 * Created by ivan on 6/3/15.
 */
public class ChooseDateTimeDialog extends DialogFragment {

    private OrderShop orderShop;
    private OrdersHandler handler;

    public static ChooseDateTimeDialog newInstance(OrderShop orderShop, OrdersHandler handler){
        ChooseDateTimeDialog dialog = new ChooseDateTimeDialog();
        dialog.orderShop = orderShop;
        dialog.handler = handler;

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_choose_datetime, container);

        Button save = (Button) view.findViewById(R.id.button_save_order_datetime);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner dates = (Spinner) view.findViewById(R.id.dates_spinner);
                orderShop.delivery_datetime = dates.getSelectedItem().toString();

                Spinner hours = (Spinner) view.findViewById(R.id.hours_spinner);
                orderShop.delivery_datetime += "|" + hours.getSelectedItem().toString();

                orderShop.save();

                dismiss();
                handler.afterDatetime();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }


}
