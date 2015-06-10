package com.mevoyalsuper.userapp.tabs.items;

import android.os.AsyncTask;
import android.util.Log;

import com.mevoyalsuper.userapp.models.Product;
import com.mevoyalsuper.userapp.utils.ConnectionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ivan on 5/27/15.
 */
public class ItemsDataHandler {

    public static void goGetItems() {
        Product.deleteAll(Product.class);
        new UpdateData().execute(ConnectionHelper.API_WEB_SERVER_ADDRESS);
    }

    private static class UpdateData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {

                String result = ConnectionHelper.getUrl(urls[0]);

                try {
                    JSONArray array = new JSONArray(result);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);

                        String sku = row.getString("sku");

                        Product product = Product.findBySku(sku);
                        product = product == null ? new Product(sku) : product;

                        product.name = row.getString("name");
                        product.tags = row.getString("tags");
                        product.price = row.getDouble("price");
                        product.img = (String) row.get("img");
                        product.apiId = row.getString("id");

                        product.save();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "DONE";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("AUX", "DONE refresh items");
        }
    }


}
