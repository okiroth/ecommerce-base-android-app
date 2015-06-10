package com.mevoyalsuper.userapp.tabs.dashboard.address;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.Address;
import com.mevoyalsuper.userapp.tabs.cart.AddCartDialog;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddressListFragment extends Fragment {

    private View rootView;
    private AddressListAdapter addressListAdapter;

    public AddressListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_address_list, container, false);

        final AddressListFragment _this = this;

        Button addAddress = (Button) rootView.findViewById(R.id.button_add_address);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addAddressDialog = AddAddressDialog.newInstance();
                addAddressDialog.setTargetFragment(_this, AddCartDialog.REQUEST_CODE);
                addAddressDialog.show(getFragmentManager(), "dialog");
            }
        });

        addressListAdapter = new AddressListAdapter(getActivity());
        ListView listView = (ListView) rootView.findViewById(R.id.address_list);
        listView.setAdapter(addressListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Address item = (Address) addressListAdapter.getItem(position);
                showDialog(item);
            }
        });

        refreshAddressList();

        return rootView;
    }

    private void refreshAddressList(){
        addressListAdapter.clear();
        addressListAdapter.addAll(Address.listAll(Address.class));
        addressListAdapter.notifyDataSetChanged();
    }


    private void showDialog(final Address address) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setMessage("Desea borrar la dirección: " + address.street + "?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                        address.delete();
                        refreshAddressList();
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        refreshAddressList();
        Toast.makeText(getActivity(), "Cambios guardados correctamente!", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }
}
