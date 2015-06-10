package com.mevoyalsuper.userapp.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.tabs.dashboard.address.AddressListFragment;
import com.mevoyalsuper.userapp.tabs.dashboard.payments.CreditCardFragment;
import com.mevoyalsuper.userapp.tabs.dashboard.userdata.UserdataFragment;


public class ActivityShowFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fragment);

        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        int code = getIntent().getIntExtra("FRAGMENT_CODE", GlobalValues.FRAG_NULL);
        Fragment frag = null;

        switch (code){
            case GlobalValues.FRAG_ADDRESS:
                setTitle("Direcciónes");
                frag = new AddressListFragment();
                break;

            case GlobalValues.FRAG_USERDATA:
                setTitle("Contacto");
                frag = new UserdataFragment();
                break;

            case GlobalValues.FRAG_CREDIT:
                setTitle("Tarjeta de Crédito");
                frag = new CreditCardFragment();
                break;
        }

        addFragment(R.id.layout_fragment_container, frag, "FRAGMENT_SHOW_TAG");
    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }
}
