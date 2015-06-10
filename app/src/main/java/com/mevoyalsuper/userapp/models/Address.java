package com.mevoyalsuper.userapp.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by ivan on 3/20/15.
 */
public class Address extends SugarRecord<Address> {

    public String city;
    public String zip;
    public String street;

    // User token
    public String user;

    public Address() {
    }

    public static String getMainAddress() {
        List<Address> list = Address.listAll(Address.class);
        if(list.size() > 0){
            return list.get(0).street + ", " + list.get(0).city;
        }
        return null;
    }
}

