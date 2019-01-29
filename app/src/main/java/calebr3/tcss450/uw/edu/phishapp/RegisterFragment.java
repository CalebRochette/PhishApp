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

import org.json.JSONException;
import org.json.JSONObject;

import calebr3.tcss450.uw.edu.phishapp.model.Credentials;
import calebr3.tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;


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
    private EditText fname;
    private EditText lname;
    private EditText username;

    private Credentials mCredentials;

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
        fname = v.findViewById(R.id.edit_register_fname);
        lname = v.findViewById(R.id.edit_register_lname);
        username = v.findViewById(R.id.edit_register_username);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBlogPostFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleRegisterOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsyncTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            if (success) {
                //register was successful. Switch to the loadSuccessFragment.
                mListener.onRegisterSuccess(mCredentials);
                return;
            } else {
                //register was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.edit_register_email))
                        .setError("Register Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.edit_register_email))
                    .setError("Register Unsuccessful");
        }
    }

    @Override
    public void onClick(View v) {
        String e = email.getText().toString();
        String p = pass.getText().toString();
        String pc = passConf.getText().toString();
        String fn = fname.getText().toString();
        String ln = lname.getText().toString();
        String un = username.getText().toString();

        if(!e.isEmpty()
                && e.contains("@")
                && !fn.isEmpty()
                && !ln.isEmpty()
                && !p.isEmpty()
                && !un.isEmpty()
                && p.length() > 5
                && p.equals(pc)){
            Credentials credentials = new Credentials.Builder(
                    e,
                    p)
                    .addFirstName(fn)
                    .addLastName(ln)
                    .addUsername(un)
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_register))
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleRegisterOnPre)
                    .onPostExecute(this::handleRegisterOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();

        } else {
            if(e.isEmpty()) email.setError("Email must not be blank");
            else if(!e.contains("@")) email.setError("Must be a valid email");
            if(p.isEmpty()) pass.setError("Password must not be blank");
            else if(!p.equals(pc)) passConf.setError("Passwords must match");
            else if(p.length() < 6) pass.setError("Password must be longer than 6 characters");
            if(fn.isEmpty()) fname.setError("First name must not be blank");
            if(ln.isEmpty()) lname.setError("Last name must not be blank");
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
    public interface OnRegisterFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void onRegisterSuccess(Credentials cred);
    }
}
