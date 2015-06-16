package com.mevoyalsuper.userapp.models;

import com.google.common.base.Joiner;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.mevoyalsuper.userapp.utils.GlobalValues;
import com.mevoyalsuper.userapp.utils.Utils;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ivan on 3/20/15.
 */
public class OrderShop extends SugarRecord<OrderShop>{

    @Ignore
    public static String LOOPBACK_NAME = "Order";

//    /** CART **/
    @Ignore
    public List<String> products;
    @Ignore
    public List<Integer> amounts;
//
//    // Supported types by ORM sugar
    private String productsSave;
    private String amountsSave;

    /** ORDER DATA **/
    public Integer status;
    public Integer payment;
    public Double charge_items;
    public Double charge_delivery;
    public String delivery_datetime;
    public String date;

    /** USER DATA **/
    public String address;
    public String receiver_name;
    public String owner;
    public String timestamp;
    public String token;
    public String paytoken;
    public String paymethod;

    public OrderShop() {
        this.products = new LinkedList<>();
        this.amounts = new LinkedList<>();
    }

    public static List<OrderShop> getAllOrders(){
        List<OrderShop> list = Select.from(OrderShop.class)
                .where(Condition.prop("token").eq(User.getLoggedUser().token))
                .orderBy("timestamp DESC")
                .list();

        return list;
    }

    /**
     * Prepares a new OrderShop from a list of Product
     * @param products
     */
    public void prepareNewOrder(List<Product> products){
        this.charge_items = 0.0;

        for(Product product : products){
            this.products.add(product.sku);
            this.amounts.add(product.cart);
            this.charge_items += (product.price * product.cart);
        }
        this.charge_delivery = GlobalValues.DELIVERY_FEE;

        this.status = GlobalValues.STATUS_NEW;
        this.payment = GlobalValues.PAYMENT_PENDING;

        this.date = Utils.getToday();

        // UserData is pre populated when creating a new order
        this.address = Address.getMainAddress();
        this.receiver_name = User.getLoggedUser().name;
        this.owner = User.getLoggedUser().email;

        this.token = User.getLoggedUser().token;
    }

    @Override
    public void save() {
        this.productsSave = Joiner.on("|").join(this.products);
        this.amountsSave = Joiner.on("|").join(this.amounts);
        super.save();
    }


    public Map getOrderMap() {
        Map data = new HashMap<>();

        data.put("products", this.products);
        data.put("amounts", this.amounts);

        data.put("status", this.status);
        data.put("payment", this.payment);
        data.put("charge_items", this.charge_items);
        data.put("charge_delivery", this.charge_delivery);

        data.put("address", this.address);
        data.put("receiver_name", this.receiver_name);
        data.put("owner", this.owner);
        data.put("delivery_datetime", this.delivery_datetime);

        data.put("token", this.token);
        data.put("paytoken", this.paytoken);
        data.put("paymethod", this.paymethod);

        return data;
    }

}

