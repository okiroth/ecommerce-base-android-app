package com.mevoyalsuper.userapp.tabs.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.CreditCard;
import com.mevoyalsuper.userapp.models.OrderShop;
import com.mevoyalsuper.userapp.models.Product;
import com.mevoyalsuper.userapp.utils.ConnectionHelper;
import com.mevoyalsuper.userapp.utils.GlobalValues;
import com.mevoyalsuper.userapp.utils.Utils;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ivan on 6/8/15.
 */
public class OrdersHandler {

    private ProgressDialog progress;
    private CartListFragment cartListFragment;
    private Context context;

    private OrderShop orderShop;

    public OrdersHandler(CartListFragment cartListFragment) {
        this.cartListFragment = cartListFragment;
        this.context = this.cartListFragment.getActivity();
    }

    /**
     * Places a new OrderShop on the back end
     */
    public void placeNewOrder() {
        if (Utils.userDataIsCompleted()) {
            orderShop = new OrderShop();
            orderShop.prepareNewOrder(Product.getCartItems());

            Long tsLong = System.currentTimeMillis()/1000;
            orderShop.timestamp = tsLong.toString();

            ChooseDateTimeDialog dateTimeDialog = ChooseDateTimeDialog.newInstance(orderShop, this);
            dateTimeDialog.show(cartListFragment.getFragmentManager(), "dialog");
        } else {
            Utils.showCompleteUserDataDialog(cartListFragment.getActivity());
        }
    }


    /**
     * Executed after the user accepts the selected delivery datetime
     */
    public void afterDatetime() {
        progress = ProgressDialog.show(context, null, "Procesando la compra", true);
        startPaymentProcess();
    }


    public void startPaymentProcess() {

        CreditCard card = CreditCard.getMainCard();

        CardToken cardToken = new CardToken(
                card.number,
                Integer.valueOf(card.exp.substring(0, 2)), //month
                Integer.valueOf(card.exp.substring(2, 4)), // year
                card.ccv,
                card.name,
                CreditCard.DNI_LABEL,
                card.dni);

        createTokenAsync(cardToken, card);
    }

    private void createTokenAsync(final CardToken cardToken, final CreditCard card) {

        MercadoPago mp = new MercadoPago.Builder()
                .setContext(context)
                .setPublicKey(GlobalValues.MP_MERCHANT_PUBLIC_KEY)
                .build();

        mp.createToken(cardToken, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                pay(card.type, token);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("AUX", "err creating token: " + error.getMessage());
                failPlaceOrder();
            }
        });
    }

    private void pay(String paymentMethod, Token token) {
        orderShop.paymethod = paymentMethod;
        orderShop.paytoken = token.getId();
        orderShop.save();

        RestAdapter adapter = new RestAdapter(context, GlobalValues.WEB_BASE_API);
        final ModelRepository repository = adapter.createRepository(OrderShop.LOOPBACK_NAME);
        final Model orderApi = repository.createObject(orderShop.getOrderMap());

        orderApi.invokeMethod(
                "create",
                orderShop.getOrderMap(),
                new Adapter.JsonObjectCallback() {

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Log.d("AUX", "err saving api: " + t.getMessage());
                        failPlaceOrder();
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        String paytoken = (String) response.opt("paytoken");
                        Integer payment = (Integer) response.opt("payment");

                        orderShop.paytoken = paytoken;
                        orderShop.payment = payment;
                        orderShop.save();

                        if(payment == GlobalValues.PAYMENT_DONE){
                            successPlaceOrder();
                        }else{
                            failPlaceOrder();
                        }
                    }
                });
    }


    private void failPlaceOrder() {
        progress.dismiss();
        Toast.makeText(cartListFragment.getActivity(), "Hubo un error, intenta nuevamente.", Toast.LENGTH_SHORT).show();
    }

    private void successPlaceOrder() {
        orderShop.payment = GlobalValues.PAYMENT_DONE;
        orderShop.save();

        // Loopback
        RestAdapter adapter = new RestAdapter(context, GlobalValues.WEB_BASE_API);
        ModelRepository repository = adapter.createRepository(OrderShop.LOOPBACK_NAME);
        Model orderApi = repository.createObject(orderShop.getOrderMap());

        orderApi.save(new VoidCallback() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                showSuccessDialog();
            }

            @Override
            public void onError(Throwable t) {
                failPlaceOrder();
            }
        });
    }


    private void showSuccessDialog(){
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
