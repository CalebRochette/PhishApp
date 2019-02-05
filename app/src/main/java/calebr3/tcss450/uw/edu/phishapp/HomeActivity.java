package calebr3.tcss450.uw.edu.phishapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import calebr3.tcss450.uw.edu.phishapp.blog.BlogPost;
import calebr3.tcss450.uw.edu.phishapp.setlists.SetList;
import calebr3.tcss450.uw.edu.phishapp.model.Credentials;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BlogFragment.OnListFragmentInteractionListener,
        BlogPostFragment.OnBlogPostFragmentInteractionListener, WaitFragment.OnFragmentInteractionListener,
        SetListsFragment.OnListFragmentInteractionListener, SetListFragment.OnFragmentInteractionListener{

    private Credentials cred;

    private String mJwToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mJwToken = getIntent().getStringExtra(getString(R.string.keys_intent_jwt));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (findViewById(R.id.frame_home) != null) {
            Bundle args = new Bundle();
            Intent intent = getIntent();
            SuccessFragment sf = new SuccessFragment();
            cred = (Credentials) intent.getSerializableExtra(getString(R.string.login_key));
            args.putSerializable(getString(R.string.login_key), cred);
            sf.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_home, sf)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_success_view) {
            SuccessFragment sf = new SuccessFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.login_key), cred);
            sf.setArguments(args);
            loadFragment(sf);
        } else if (id == R.id.nav_blog_view) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_blog))
                    .appendPath(getString(R.string.ep_get))
                    .build();
            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleBlogGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build().execute();
        } else if (id == R.id.nav_set_lists) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_setlists))
                    .appendPath(getString(R.string.ep_recent))
                    .build();
            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleSetListGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build().execute();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(BlogPost blog) {
        Bundle args = new Bundle();
        args.putCharSequence(getString(R.string.blog_key_title),blog.getTitle());
        args.putCharSequence(getString(R.string.blog_key_teaser),blog.getTeaser());
        args.putCharSequence(getString(R.string.blog_key_author),blog.getAuthor());
        args.putCharSequence(getString(R.string.blog_key_url),blog.getUrl());
        args.putCharSequence(getString(R.string.blog_key_publish_date),blog.getPubDate());
        BlogPostFragment bpf = new BlogPostFragment();
        bpf.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_home, bpf)
                .addToBackStack(null);
        transaction.commit();


    }

    private void handleBlogGetOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));
                    List<BlogPost> blogs = new ArrayList<>();
                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        blogs.add(new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_url)))
                                .build());
                    }
                    BlogPost[] blogsAsArray = new BlogPost[blogs.size()];
                    blogsAsArray = blogs.toArray(blogsAsArray);
                    Bundle args = new Bundle();
                    args.putSerializable(BlogFragment.ARG_BLOG_LIST, blogsAsArray);
                    Fragment frag = new BlogFragment();
                    frag.setArguments(args);
                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void handleSetListGetOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));
                    List<SetList> setLists = new ArrayList<>();
                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonSetList = data.getJSONObject(i);
                        setLists.add(new SetList.Builder(
                                jsonSetList.getString(
                                        getString(R.string.keys_json_set_list_long_date)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_set_list_venue)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_set_list_location)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_set_list_data)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_set_list_notes)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_set_list_url))
                                )
                                .build());
                    }
                    SetList[] setListsAsArray = new SetList[setLists.size()];
                    setListsAsArray = setLists.toArray(setListsAsArray);
                    Bundle args = new Bundle();
                    args.putSerializable(SetListsFragment.ARG_SET_LISTS, setListsAsArray);
                    Fragment frag = new SetListsFragment();
                    frag.setArguments(args);
                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_home, frag)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBlogPostFragmentInteraction(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_home, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();

    }

    @Override
    public void onListFragmentInteraction(SetList item) {
        Bundle args = new Bundle();
        SetListFragment slf = new SetListFragment();
        args.putCharSequence(getString(R.string.keys_set_list_locations), item.getLocation());
        args.putCharSequence(getString(R.string.keys_set_list_venue), item.getVenue());
        args.putCharSequence(getString(R.string.keys_set_list_long_date), item.getLongDate());
        args.putCharSequence(getString(R.string.keys_set_list_data), item.getSetListData());
        args.putCharSequence(getString(R.string.keys_set_list_notes), item.getSetListNotes());
        args.putCharSequence(getString(R.string.keys_set_list_url), item.getUrl());
        slf.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_home, slf)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSetListFragmentInteraction(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
