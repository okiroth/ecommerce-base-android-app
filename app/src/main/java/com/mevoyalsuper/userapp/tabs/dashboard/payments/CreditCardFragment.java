package com.mevoyalsuper.userapp.tabs.dashboard.payments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.CreditCard;
import com.mevoyalsuper.userapp.models.User;
import com.mevoyalsuper.userapp.utils.FourDigitCardFormatWatcher;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreditCardFragment extends Fragment {

    private View rootView;

    public CreditCardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.block_creditcard, container, false);

        Button saveCard = (Button) rootView.findViewById(R.id.button_save_card);
        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCardData();
            }
        });
        EditText cardNumber = (EditText) rootView.findViewById(R.id.userdata_card_number);
        cardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());

        CreditCard card = CreditCard.getMainCard();
        if(card != null){
            ((EditText) rootView.findViewById(R.id.userdata_card_name)).setText(card.name);
            ((EditText) rootView.findViewById(R.id.userdata_card_dni)).setText(card.dni);
            ((EditText) rootView.findViewById(R.id.userdata_card_ccv)).setText(card.ccv);
            cardNumber.setText(card.number);
            ((EditText) rootView.findViewById(R.id.userdata_card_exp)).setText(card.exp);
        }

        return rootView;
    }


    private void saveCardData(){
        EditText number = (EditText) rootView.findViewById(R.id.userdata_card_number);
        EditText ccv = (EditText) rootView.findViewById(R.id.userdata_card_ccv);
        EditText exp = (EditText) rootView.findViewById(R.id.userdata_card_exp);
        EditText name = (EditText) rootView.findViewById(R.id.userdata_card_name);

        CreditCard card = CreditCard.getMainCard();
        if(card == null) card = new CreditCard();

        card.name = name.getText().toString();
        card.ccv = ccv.getText().toString();
        card.number = number.getText().toString();
        card.exp = exp.getText().toString();

        card.user = User.getLoggedUser().token;

        card.save();
        Toast.makeText(getActivity(), "Cambios guardados correctamente!", Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }
}
