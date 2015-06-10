package com.mevoyalsuper.userapp.tabs.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.Product;
import com.mevoyalsuper.userapp.models.OrderShop;
import com.mevoyalsuper.userapp.utils.ConnectionHelper;
import com.mevoyalsuper.userapp.utils.GlobalValues;
import com.mevoyalsuper.userapp.utils.Utils;

/**
 * Created by ivan on 6/8/15.
 */
public class OrdersHandler {

    private ProgressDialog progress;
    private CartListFragment cartListFragment;
    private Context context;

    public OrdersHandler(CartListFragment cartListFragment){
        this.cartListFragment = cartListFragment;
        this.context = this.cartListFragment.getActivity();
    }

    /**
     * Places a new OrderShop on the back end
     */
    public void placeNewOrder(){
        if(Utils.userDataIsCompleted()){
            OrderShop orderShop = new OrderShop();
            orderShop.prepareNewOrder(Product.getCartItems());

            ChooseDateTimeDialog dateTimeDialog = ChooseDateTimeDialog.newInstance(orderShop, this);
            dateTimeDialog.show(cartListFragment.getFragmentManager(), "dialog");
        }else{
            Utils.showCompleteUserDataDialog(cartListFragment.getActivity());
        }
    }


    /**
     * Executed after the user accepts the selected delivery datetime
     * @param orderShop
     */
    public void afterDatetime(final OrderShop orderShop) {
        // Loopback
        RestAdapter adapter = new RestAdapter(context, ConnectionHelper.API_WEB_SERVER_ADDRESS);
        ModelRepository repository = adapter.createRepository(OrderShop.LOOPBACK_NAME);
        Model orderApi = repository.createObject(orderShop.getOrderMap());

        CharSequence title = null;
        progress = ProgressDialog.show(context, title, "Procesando la compra", true);

        if(payOrder(orderShop)){
            orderApi.save(new VoidCallback() {
                @Override
                public void onSuccess() {
                    progress.dismiss();
                    successPlaceOrder(orderShop);
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    Toast.makeText(cartListFragment.getActivity(), "Hubo un error, intenta nuevamente.", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            progress.dismiss();
            Toast.makeText(cartListFragment.getActivity(), "Hubo un error, intenta nuevamente.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * TODO PAY! use MP
     *
     * @param orderShop
     *
     * @return boolean success or not
     */
    private boolean payOrder(OrderShop orderShop) {
//        double amount = orderShop.charge_items + orderShop.charge_delivery;
        orderShop.payment = GlobalValues.PAYMENT_DONE;

        orderShop.save();

        return true;
    }

    private void successPlaceOrder(OrderShop orderShop){
        orderShop.save();

        new AlertDialog.Builder(cartListFragment.getActivity())
        .setTitle("Nos fuimos al super!")
                .setMessage("Tus compras llegarán " + orderShop.delivery_datetime)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Product.emptyCart();
                        cartListFragment.refreshCartView();
                    }
                })
                .setIcon(R.drawable.ic_shopping_cart_black_24dp)
                .show();
    }

}
