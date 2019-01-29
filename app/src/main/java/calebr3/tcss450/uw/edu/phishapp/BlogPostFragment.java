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

import calebr3.tcss450.uw.edu.phishapp.blog.BlogPost;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBlogPostFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BlogPostFragment extends Fragment implements View.OnClickListener {

    private OnBlogPostFragmentInteractionListener mListener;

    private BlogPost post;

    public BlogPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog_post, container, false);

        Bundle args = getArguments();
        BlogPost.Builder builder;
        if(args != null) {
            builder = new BlogPost.Builder((String) args.getCharSequence(getString(R.string.blog_key_publish_date)).toString()
                    ,args.getCharSequence(getString(R.string.blog_key_title)).toString());
            builder.addAuthor(args.getCharSequence(getString(R.string.blog_key_author)).toString());
            builder.addTeaser(args.getCharSequence(getString(R.string.blog_key_teaser)).toString());
            builder.addUrl(args.getCharSequence(getString(R.string.blog_key_url)).toString());
            post = builder.build();
            ((TextView)v.findViewById(R.id.blog_publish_date)).setText(post.getPubDate());
            ((TextView)v.findViewById(R.id.blog_teaser)).setText(Html.fromHtml(post.getTeaser()));
            ((TextView)v.findViewById(R.id.blog_title)).setText(post.getTitle());
            Button b = v.findViewById(R.id.button_blog_post);
            b.setOnClickListener(this);

        }



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBlogPostFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBlogPostFragmentInteractionListener) {
            mListener = (OnBlogPostFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse(post.getUrl());
        mListener.onBlogPostFragmentInteraction(uri);
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
    public interface OnBlogPostFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBlogPostFragmentInteraction(Uri uri);
    }
}
