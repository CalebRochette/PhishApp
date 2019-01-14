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

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.button_login:
                    login(view, mListener);
                    break;
                case R.id.text_register:
                    mListener.onRegisterClicked();
                    break;
                default:
                    break;
            }

        }
    }

    private static void login(View view, OnLoginFragmentInteractionListener mListener){
        String email = ((EditText) view.findViewById(R.id.edit_email)).getText().toString();
        String pass = ((EditText) view.findViewById(R.id.edit_password)).getText().toString();
        if(email.contains("@") && !pass.isEmpty()){
            Credentials.Builder b = new Credentials.Builder(email, pass);
            Credentials cred = b.build();
            mListener.onLoginSuccess(cred, null);
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
