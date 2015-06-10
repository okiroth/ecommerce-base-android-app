package com.mevoyalsuper.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mevoyalsuper.userapp.utils.ActivityShowFragment;
import com.mevoyalsuper.userapp.utils.GlobalValues;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mevoyalsuper.userapp.R.layout.activity_home);
        getSupportActionBar().hide();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(com.mevoyalsuper.userapp.R.id.viewpager);
        viewPager.setAdapter(new TabsLayoutAdapter(getSupportFragmentManager(),
                HomeActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(com.mevoyalsuper.userapp.R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void showAddressList(View view){
        Intent intent = new Intent(this, ActivityShowFragment.class);
        intent.putExtra("FRAGMENT_CODE", GlobalValues.FRAG_ADDRESS);

        startActivity(intent);
    }

    public void showUserdata(View view){
        Intent intent = new Intent(this, ActivityShowFragment.class);
        intent.putExtra("FRAGMENT_CODE", GlobalValues.FRAG_USERDATA);

        startActivity(intent);
    }

    public void showCreditCard(View view){
        Intent intent = new Intent(this, ActivityShowFragment.class);
        intent.putExtra("FRAGMENT_CODE", GlobalValues.FRAG_CREDIT);

        startActivity(intent);
    }
}
