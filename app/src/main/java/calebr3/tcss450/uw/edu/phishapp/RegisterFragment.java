package calebr3.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import calebr3.tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private OnRegisterFragmentInteractionListener mListener;

    private EditText email;
    private EditText pass;
    private EditText passConf;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button b = v.findViewById(R.id.button_register);
        b.setOnClickListener(this);

        email = v.findViewById(R.id.edit_register_email);
        pass = v.findViewById(R.id.edit_register_password);
        passConf = v.findViewById(R.id.edit_register_password_confirm);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        String e = email.getText().toString();
        String p = pass.getText().toString();
        String pc = passConf.getText().toString();

        if(!e.isEmpty()
                && e.contains("@")
                && !p.isEmpty()
                && p.length() > 5
                && p.equals(pc)){
            Credentials.Builder b = new Credentials.Builder(e, p);
            mListener.onRegisterSuccess(b.build());
        } else {
            if(e.isEmpty()) email.setError("Email must not be blank");
            else if(!e.contains("@")) email.setError("Must be a valid email");
            if(p.isEmpty()) pass.setError("Password must not be blank");
            else if(!p.equals(pc)) passConf.setError("Passwords must match");
            else if(p.length() < 6) pass.setError("Password must be longer than 6 characters");
        }

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
    public interface OnRegisterFragmentInteractionListener {
        // TODO: Update argument type and name

        void onRegisterSuccess(Credentials cred);
    }
}
