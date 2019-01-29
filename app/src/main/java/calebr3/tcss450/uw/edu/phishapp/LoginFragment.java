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
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnLoginFragmentInteractionListener mListener;

    private static final String TAG = "FILTER_LOGIN";

    private EditText email;
    private EditText pass;

    private Credentials mCredentials;

    public LoginFragment() {
        // Required empty public constructor
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
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.
                mListener.onLoginSuccess(mCredentials,
                        resultsJSON.getString(
                                getString(R.string.keys_json_login_jwt)));
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.edit_email))
                        .setError("Login Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.edit_email))
                    .setError("Login Unsuccessful");
        }
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.button_login:
                    boolean hasError = false;
                    if(email.getText().toString().contains("@") && !pass.getText().toString().isEmpty()){
                        Credentials credentials = new Credentials.Builder(
                                email.getText().toString(),
                                pass.getText().toString())
                                .build();
                        //build the web service URL
                        Uri uri = new Uri.Builder()
                                .scheme("https")
                                .appendPath(getString(R.string.ep_base_url))
                                .appendPath(getString(R.string.ep_login))
                                .build();
                        //build the JSONObject
                        JSONObject msg = credentials.asJSONObject();
                        mCredentials = credentials;
                        //instantiate and execute the AsyncTask.
                        new SendPostAsyncTask.Builder(uri.toString(), msg)
                                .onPreExecute(this::handleLoginOnPre)
                                .onPostExecute(this::handleLoginOnPost)
                                .onCancelled(this::handleErrorsInTask)
                                .build().execute();
                    } else {
                        if(email.getText().toString().isEmpty()){
                            email.setError("Email must not be blank");
                        }
                        else if(!email.getText().toString().contains("@")){
                            email.setError("Must be a valid email");

                        }
                        if(pass.getText().toString().isEmpty()){
                            pass.setError("Password must not be blank");
                        }
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
                    + " must implement OnBlogPostFragmentInteractionListener");
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
    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {

        void onLoginSuccess(Credentials cred, String jwt);

        void onRegisterClicked();
    }
}
