package calebr3.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import calebr3.tcss450.uw.edu.phishapp.setlists.SetList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SetListFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private SetList mSetList;

    public SetListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_list, container, false);

        Bundle args = getArguments();;
        if(args != null) {

            mSetList = new SetList.Builder(args.getCharSequence(getString(R.string.keys_set_list_long_date)).toString()
                    ,args.getCharSequence(getString(R.string.keys_set_list_venue)).toString()
                    ,args.getCharSequence(getString(R.string.keys_set_list_locations)).toString()
                    ,args.getCharSequence(getString(R.string.keys_set_list_data)).toString()
                    ,args.getCharSequence(getString(R.string.keys_set_list_notes)).toString()
                    ,args.getCharSequence(getString(R.string.keys_set_list_url)).toString())
                .build();
            ((TextView)v.findViewById(R.id.text_location)).setText(mSetList.getLocation());
            TextView t = ((TextView)v.findViewById(R.id.text_date));
            t.setText(mSetList.getLongDate());
            ((TextView)v.findViewById(R.id.text_data)).setText(Html.fromHtml(mSetList.getSetListData()));
            ((TextView)v.findViewById(R.id.text_notes)).setText(Html.fromHtml(mSetList.getSetListNotes()));
            Button b = v.findViewById(R.id.button_open_list);
            b.setOnClickListener(this);

        }
        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSetListFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        Uri uri = Uri.parse(mSetList.getUrl());
        mListener.onSetListFragmentInteraction(uri);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSetListFragmentInteraction(Uri uri);
    }
}
