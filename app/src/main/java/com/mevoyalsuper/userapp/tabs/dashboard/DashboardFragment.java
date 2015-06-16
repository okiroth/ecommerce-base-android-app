package com.mevoyalsuper.userapp.tabs.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mevoyalsuper.userapp.models.OrderShop;
import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.utils.Utils;

/**
 * Created by ivan on 5/27/15.
 */
public class DashboardFragment extends Fragment {

    public View rootView;


    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if(Utils.userDataIsCompleted()){
            rootView.findViewById(R.id.complete_userdata_legend).setVisibility(View.GONE);
        }

        // Populate the Order history list
        OrderHistoryListAdapter adapter = new OrderHistoryListAdapter(getActivity());
        ListView listView = (ListView) rootView.findViewById(R.id.order_history_list);
        listView.setAdapter(adapter);

        adapter.clear();
        adapter.addAll(OrderShop.getAllOrders());
        adapter.notifyDataSetChanged();

        return rootView;
    }

}

