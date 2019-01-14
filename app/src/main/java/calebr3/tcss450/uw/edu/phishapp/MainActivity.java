package calebr3.tcss450.uw.edu.phishapp;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import calebr3.tcss450.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener, RegisterFragment.OnRegisterFragmentInteractionListener {

    private static final String TAG = "FILTER_MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (findViewById(R.id.frame_main_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_container, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void onLoginSuccess(Credentials cred, String jwt) {
//        SuccessFragment sf;
//        sf = new SuccessFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(getString(R.string.all_color_key), color);
//        lf.setArguments(args);
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_main_container, sf)
//                .addToBackStack(null);
//        // Commit the transaction
//        transaction.commit();

    }

    @Override
    public void onRegisterClicked() {
        RegisterFragment rf;
        rf = new RegisterFragment();
        Bundle args = new Bundle();

        rf.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, rf)
                .addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onRegisterSuccess(Credentials cred) {

    }
}
