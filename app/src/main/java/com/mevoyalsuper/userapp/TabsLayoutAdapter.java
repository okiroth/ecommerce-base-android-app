package com.mevoyalsuper.userapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.mevoyalsuper.userapp.tabs.cart.CartListFragment;
import com.mevoyalsuper.userapp.tabs.dashboard.DashboardFragment;
import com.mevoyalsuper.userapp.tabs.items.ItemsGridFragment;

/**
 * Created by ivan on 6/3/15.
 */
public class TabsLayoutAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private Context context;

    private int[] imageResId = {
            com.mevoyalsuper.userapp.R.drawable.ic_action_store_128,
            com.mevoyalsuper.userapp.R.drawable.ic_shopping_cart_white_24dp,
            com.mevoyalsuper.userapp.R.drawable.ic_dashboard_white_24dp
    };

    public TabsLayoutAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return ItemsGridFragment.newInstance();

            case 1:
                return CartListFragment.newInstance();

            case 2:
                return DashboardFragment.newInstance();

        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = context.getResources().getDrawable(imageResId[position], null);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }


}