package com.mevoyalsuper.userapp.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by ivan on 3/20/15.
 */
public class Product extends SugarRecord<Product> {

    @Ignore
    public static String LOOPBACK_NAME = "Item";

    // Set by default
    public Integer cart;
    public String  sku;

    public String  name;
    public String  tags;
    public Double  price;
    public String  img;
    public String  apiId;

    public Product() {
    }

    public Product(String sku){
        this.cart = 0;
        this.sku = sku;
    }

    public static Product findBySku(String sku) {
        return findByKey("sku", sku);
    }

    public static Product findByKey(String key, String value){
        List<Product> list = Select.from(Product.class)
                .where(Condition.prop(key).eq(value))
                .list();

        if (list.size() != 1) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static List<Product> getCartItems() {
        return Select.from(Product.class)
                .where(Condition.prop("cart").notEq(0))
                .list();
    }

    public boolean isInCart() {
        return cart > 0;
    }

    public String getCartAmount() {
        return this.isInCart() ? this.cart + "" : "";
    }

    public static boolean isEmpty() {
        return Select.from(Product.class).count() == 0 ? true : false;
    }

    public static void emptyCart() {
        if(isEmpty()) return;

        for(Product product : getCartItems()){
            product.cart = 0;
            product.save();
        }
    }
}

