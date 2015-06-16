package com.mevoyalsuper.userapp.tabs.cart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Item;
import com.mercadopago.model.MerchantPayment;
import com.mercadopago.model.Payment;
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

import java.math.BigDecimal;
import java.util.List;

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

        // Set card token
        CardToken cardToken = new CardToken(
                card.number,
                Integer.valueOf(card.exp.substring(0, 2)), //month
                Integer.valueOf(card.exp.substring(2, 4)), // year
                card.ccv,
                card.name,
                CreditCard.DNI_LABEL,
                card.dni);

        // Create token
        createTokenAsync(cardToken);
    }

    private void createTokenAsync(final CardToken cardToken) {

        // Init MercadoPago object with public key
        final MercadoPago mp = new MercadoPago.Builder()
                .setContext(context)
                .setPublicKey(GlobalValues.MP_MERCHANT_PUBLIC_KEY)
                .build();


        mp.getPaymentMethods(new Callback<List<PaymentMethod>>() {
            public PaymentMethod paymentMethod;


            @Override
            public void success(List<PaymentMethod> paymentMethods, Response response) {

                for (PaymentMethod method : paymentMethods) {
                    if (method.getName().toLowerCase().equals("visa")) {
                        paymentMethod = method;
                        break;
                    }
                }

                if (paymentMethod == null) {
                    failPlaceOrder();
                    return;
                }

                mp.createToken(cardToken, new Callback<Token>() {
                    @Override
                    public void success(Token token, Response response) {

                        createPayment(
                                cartListFragment.getActivity(),
                                token.getId(),
                                1,
                                null,
                                paymentMethod, null);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("AUX", "err creating token");
                        failPlaceOrder();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("AUX", "err getting methods");
                failPlaceOrder();
            }
        });


    }

    public void createPayment(final Activity activity, String token, Integer installments, Long cardIssuerId, final PaymentMethod paymentMethod, Discount discount) {

        if (paymentMethod != null) {

            // Set item
            Integer quantity = 1;
            BigDecimal price = BigDecimal.valueOf(orderShop.charge_items + orderShop.charge_delivery);
            Item item = new Item("Compra de productos en mevoyalsuper.com", quantity, price);

            // Set payment method id
            String paymentMethodId = paymentMethod.getId();

            // Set campaign id
            Long campaignId = (discount != null) ? discount.getId() : null;

            // Set merchant payment
            MerchantPayment payment = new MerchantPayment(item, installments, cardIssuerId,
                    token, paymentMethodId, campaignId, GlobalValues.MP_MERCHANT_ACCESS_TOKEN);

            // Create payment
            MerchantServer.createPayment(activity,
                    GlobalValues.MP_MERCHANT_BASE_URL,
                    GlobalValues.MP_MERCHANT_CREATE_PAYMENT_URI,
                    payment, new Callback<Payment>() {

                        @Override
                        public void success(Payment payment, Response response) {
                            if(payment.getStatus().equals("OK")){
                                successPlaceOrder();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("AUX", "err: " + error.getMessage());
                            failPlaceOrder();
                        }
                    });
        } else {
            Log.d("AUX", "err method was null");
            failPlaceOrder();
        }
    }

    private void failPlaceOrder() {
        progress.dismiss();
        Toast.makeText(cartListFragment.getActivity(), "Hubo un error, intenta nuevamente.", Toast.LENGTH_SHORT).show();
    }

    private void successPlaceOrder() {
        orderShop.payment = GlobalValues.PAYMENT_DONE;
        orderShop.save();

        // Loopback
        RestAdapter adapter = new RestAdapter(context, ConnectionHelper.API_WEB_SERVER_ADDRESS);
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
