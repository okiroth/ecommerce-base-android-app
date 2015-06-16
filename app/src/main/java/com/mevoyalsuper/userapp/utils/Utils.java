package com.mevoyalsuper.userapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mevoyalsuper.userapp.models.Address;
import com.mevoyalsuper.userapp.models.CreditCard;
import com.mevoyalsuper.userapp.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivan on 6/10/15.
 */
public class Utils {

    public static boolean userDataIsCompleted() {
        // User contact
        User user = User.getLoggedUser();
        if(user == null)        return false;
        if(isEmpty(user.name))  return false;
        if(isEmpty(user.phone)) return false;
        if(isEmpty(user.email)) return false;

        // At least one address
        if(isEmpty(Address.getMainAddress())) return false;

        // Credit card data
        CreditCard card = CreditCard.getMainCard();
        if(card == null)         return false;
        if(isEmpty(card.number)) return false;
        if(isEmpty(card.name))  return false;
        if(isEmpty(card.ccv))   return false;
        if(isEmpty(card.exp))   return false;

        return true;
    }

    public static boolean isEmpty(String str){
        if(str == null) return true;
        if(str.trim().length() == 0) return true;

        return false;
    }

    public static void showCompleteUserDataDialog(Context context) {
        new AlertDialog.Builder(context)
                .setMessage("Completa tus datos para realizar una compra.")
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static String getToday() {
        Date date =  new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
        return dateFormat.format(date);
    }

}
