package com.mevoyalsuper.userapp.tabs.dashboard.userdata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mevoyalsuper.userapp.R;
import com.mevoyalsuper.userapp.models.User;
import com.mevoyalsuper.userapp.utils.ConnectionHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserdataFragment extends Fragment {

    private View rootView;

    public UserdataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.block_userdata, container, false);

        Button saveUser = (Button) rootView.findViewById(R.id.button_save_userdata);
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        User user = User.getLoggedUser();
        if(user != null){
            ((EditText) rootView.findViewById(R.id.userdata_edit_email)).setText(user.email);
            ((EditText) rootView.findViewById(R.id.userdata_edit_name)).setText(user.name);
            ((EditText) rootView.findViewById(R.id.userdata_edit_phone)).setText(user.phone);
        }

        return rootView;
    }

    private void saveUserData(){
        EditText editName = (EditText) rootView.findViewById(R.id.userdata_edit_name);
        EditText editEmail = (EditText) rootView.findViewById(R.id.userdata_edit_email);
        EditText editPhone = (EditText) rootView.findViewById(R.id.userdata_edit_phone);

        User user = User.findByEmail(editEmail.getText().toString());

        // Create a new user
        if(user == null){
            user = new User();
            user.token = ConnectionHelper.getRandomString();
        }

        // or update the current user data
        user.name = editName.getText().toString();
        user.email = editEmail.getText().toString();
        user.phone = editPhone.getText().toString();

        user.save();

        Toast.makeText(getActivity(), "Cambios guardados correctamente!", Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }
}
