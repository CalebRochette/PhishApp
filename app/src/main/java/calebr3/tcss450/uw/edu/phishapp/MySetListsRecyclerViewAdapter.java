package calebr3.tcss450.uw.edu.phishapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import calebr3.tcss450.uw.edu.phishapp.SetListsFragment.OnListFragmentInteractionListener;
import calebr3.tcss450.uw.edu.phishapp.setlists.SetList;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SetList} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySetListsRecyclerViewAdapter extends RecyclerView.Adapter<MySetListsRecyclerViewAdapter.ViewHolder> {

    private final List<SetList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySetListsRecyclerViewAdapter(List<SetList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_setlists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(""+(position+1));
        holder.mLocationView.setText(mValues.get(position).getLocation());
        holder.mLongDateView.setText(mValues.get(position).getLongDate());
        holder.mVenueView.setText(mValues.get(position).getVenue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mLocationView;
        public final TextView mVenueView;
        public final TextView mLongDateView;
        public SetList mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.setlist_number);
            mLocationView = (TextView) view.findViewById(R.id.location);
            mVenueView = (TextView) view.findViewById(R.id.venue);
            mLongDateView = (TextView) view.findViewById(R.id.long_date);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }
    }
}
