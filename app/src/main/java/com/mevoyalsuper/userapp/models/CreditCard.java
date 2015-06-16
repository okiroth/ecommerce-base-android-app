package com.mevoyalsuper.userapp.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by ivan on 3/20/15.
 */
public class CreditCard extends SugarRecord<CreditCard> {

    @Ignore
    public static String DNI_LABEL = "DNI";

    public String number;
    public String ccv;
    public String exp;
    public String name;
    public String dni;
    public String type;

    // User token
    public String user;

    public CreditCard() {
    }

    public static CreditCard getMainCard() {
        List<CreditCard> cards = CreditCard.listAll(CreditCard.class);
        if(cards.size() > 0) return cards.get(0);

        return null;
    }
}

