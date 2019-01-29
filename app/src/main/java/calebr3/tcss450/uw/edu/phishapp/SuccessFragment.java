package calebr3.tcss450.uw.edu.phishapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import calebr3.tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {

    private static final String TAG = "FILTER_SUCCESS";

    private TextView success;


    public SuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_success, container, false);

        Bundle args = getArguments();
        if(args != null) {
            Credentials cred = (Credentials) args.getSerializable(getString(R.string.login_key));
            Log.d(TAG, "oops");
            String email = cred.getEmail();
            success = (TextView) v.findViewById(R.id.text_success);
            success.setText(email);

        }
        return v;
    }

}
