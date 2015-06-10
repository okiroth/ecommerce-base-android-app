package com.mevoyalsuper.userapp.tabs.dashboard.address;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.Address;

/**
 * Created by ivan on 6/3/15.
 */
public class AddAddressDialog extends DialogFragment {

    private static final int REQUEST_CODE = 4;

    public static AddAddressDialog newInstance() {
        AddAddressDialog frag = new AddAddressDialog();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_add_address, container);

        Button save = (Button) view.findViewById(R.id.button_save_new_address);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Address address = new Address();
                EditText street = (EditText) view.findViewById(R.id.add_address_street);
                address.street = street.getText().toString();

                Spinner cities = (Spinner) view.findViewById(R.id.cities_spinner);
                address.city = cities.getSelectedItem().toString();

                address.save();

                getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, getActivity().getIntent());
                dismiss();
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
