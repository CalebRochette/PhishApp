package calebr3.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import calebr3.tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnLoginFragmentInteractionListener mListener;

    private static final String TAG = "FILTER_LOGIN";

    private EditText email;
    private EditText pass;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.button_login:
                    if(email.getText().toString().contains("@") && !pass.getText().toString().isEmpty()){
                        Credentials.Builder b = new Credentials.Builder(email.getText().toString(), pass.getText().toString());
                        Credentials cred = b.build();
                        mListener.onLoginSuccess(cred, null);
                    } else {
                        if(email.getText().toString().isEmpty()) email.setError("Email must not be blank");
                        else if(!email.getText().toString().contains("@")) email.setError("Must be a valid email");
                        if(pass.getText().toString().isEmpty()) pass.setError("Password must not be blank");
                    }
                    break;
                case R.id.text_register:
                    mListener.onRegisterClicked();
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) v.findViewById(R.id.button_login);
        b.setOnClickListener(this);

        TextView t = (TextView) v.findViewById(R.id.text_register);
        t.setOnClickListener(this);

        email = v.findViewById(R.id.edit_email);
        pass = v.findViewById(R.id.edit_password);

        Bundle args = getArguments();
        if(args != null) {
            Credentials cred = (Credentials) args.getSerializable(getString(R.string.register_key));
            email.setText(cred.getEmail());
            pass.setText(cred.getPassword());
        }

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginFragmentInteractionListener {

        void onLoginSuccess(Credentials cred, String jwt);

        void onRegisterClicked();
    }
}
