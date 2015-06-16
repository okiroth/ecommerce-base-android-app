package com.mevoyalsuper.userapp.tabs.items;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mevoyalsuper.userapp.models.OrderShop;
import com.mevoyalsuper.userapp.utils.ConnectionHelper;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.mevoyalsuper.userapp.utils.GlobalValues;
import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.tabs.cart.AddCartDialog;
import com.mevoyalsuper.userapp.models.Product;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.List;

/**
 * Created by ivan on 5/27/15.
 */
public class ItemsGridFragment extends Fragment {

    private String category = GlobalValues.ALL_CATEGORIES;
    public ItemsGridAdapter adapter;

    public GridView gridView;
    public View rootView;

    public static ItemsGridFragment newInstance() {
        return new ItemsGridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grid_items, container, false);

        adapter = new ItemsGridAdapter(getActivity());

        gridView = (GridView) rootView.findViewById(R.id.items_listview);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Product product = (Product) adapter.getItem(position);
                showDialog(product);
            }
        });

        // Link the text box
        EditText search = (EditText) rootView.findViewById(R.id.search_term);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                goSearch(s.toString());
            }
        });

        // Link spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.categories_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                EditText search = (EditText) rootView.findViewById(R.id.search_term);
                goSearch(search.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(Product.isEmpty()){
            RestAdapter adapter = new RestAdapter(getActivity(), ConnectionHelper.API_WEB_SERVER_ADDRESS);
            ModelRepository repository = adapter.createRepository(Product.LOOPBACK_NAME);

            final ProgressDialog progress = ProgressDialog.show(getActivity(), null,
                    "Actualizando productos, puede demorar unos minutos", true);

            repository.findAll(new ListCallback<Model>() {

                @Override
                public void onSuccess(final List<Model> objects) {
                    new Thread(new Runnable() {
                        public void run() {
                            int i = 0;
                            for(Model res : objects){
                                Product product = new Product((String) res.get("sku"));
                                product.name    = (String) res.get("name");
                                product.price   = Double.valueOf((String) res.get("price"));
                                product.apiId   = (String) res.get("apiId");
                                product.tags    = (String) res.get("tags");
                                product.img     = (String) res.get("img");
                                product.save();
                            }
                            progress.dismiss();
                        }
                    }).start();
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Error al actualizar productos", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showDialog(Product product) {
        DialogFragment dialogFragment = AddCartDialog.newInstance(product);
        dialogFragment.setTargetFragment(this, AddCartDialog.REQUEST_CODE);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }

    private void goSearch(String name){
        adapter.clear();

        if(name.length() < GlobalValues.SEARCH_MIN_LENGTH && category.equals(GlobalValues.ALL_CATEGORIES)){

            getView().findViewById(R.id.empty_results_legend).setVisibility(View.VISIBLE);
            return;

        }else{

            Select<Product> query = Select.from(Product.class);
            query.where(Condition.prop("name").like("%" + name + "%"));
            if(!category.equals(GlobalValues.ALL_CATEGORIES)) query.where(Condition.prop("tags").eq(category));

            List<Product> products = query.limit(GlobalValues.LIMIT_MAX_GIRD_ITEMS).list();

            if(products.size() > 0){
                getView().findViewById(R.id.empty_results_legend).setVisibility(View.GONE);
            }else{
                getView().findViewById(R.id.empty_results_legend).setVisibility(View.VISIBLE);
            }

            adapter.addAll(products);
            adapter.notifyDataSetChanged();
        }
    }
}

