package com.example.punit.popularmovies.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.punit.popularmovies.Activities.DetailActivity;
import com.example.punit.popularmovies.Activities.FilterActivity;
import com.example.punit.popularmovies.Activities.SearchActivity;
import com.example.punit.popularmovies.Adapters.FavAdapter;
import com.example.punit.popularmovies.Adapters.GridAdapter;
import com.example.punit.popularmovies.Application.AppController;
import com.example.punit.popularmovies.Database.DataProvider;
import com.example.punit.popularmovies.Database.DbHelper;
import com.example.punit.popularmovies.Helpers.ConnectionDetector;
import com.example.punit.popularmovies.Helpers.DeleteEvent;
import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Used to display Popular Movies/Highly Rated/Favorite movies depending on Filter Set from FilterActivity class
 */
public class PopularMoviesFragment extends Fragment{

    //View Initialization using ButterKnife
    @Bind(R.id.gridview) GridView gridView;
    @Bind(R.id.progressBar) ProgressBar pbar;
    @Bind(R.id.rl_internet) RelativeLayout rl_internet;
    @Bind(R.id.rl_volley) RelativeLayout rl_volley;
    @Bind(R.id.retry) Button retry;
    @Bind(R.id.retry_volley) Button retry_volley;
    @Bind(R.id.browse_movies) Button browse_movies;
    @Bind(R.id.volley_error_msg) TextView volley_error_msg;
    @Bind(R.id.no_fav_added) RelativeLayout no_fav_added;
    @Nullable @Bind(R.id.detail_fragment) FrameLayout detailFragment;//define for Dual Pane Layout

    //Instance variables
    GridAdapter adapter;
    FavAdapter favAdapter;
    ArrayList<Movie> movies;
    private MenuItem menu_item;
    TextView toolbar_title;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    DetailFragment dfrag;
    private EventBus bus = EventBus.getDefault();

    //static variables
    private static String MOVIES_URL = "http://api.themoviedb.org/3/discover/movie?api_key=";
    private static String SORT_BY_POPULAR="http://api.themoviedb.org/3/movie/popular?api_key=";
    private static String SORT_BY_RATING="http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String TAG="POPULAR";
    private static final String PREFS_NAME ="SORT_CRITERIA";
    private static final String SORT_POPULAR="POPULAR";
    private static final String SORT_RATING ="RATING";
    private static final String FILTER_FAVORITES = "FAVORITES";
    private static final int INTENT_REQ = 99;
    private static final String FAV_TITLE = "Favorites";
    private static final String TITLE= "Popular Movies";

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);//register for Delete Event (event listening to removing favorites from the list inside detail fragment)
        setHasOptionsMenu(true);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        preferences = getActivity().getSharedPreferences(PREFS_NAME,0);
        movies = new ArrayList<Movie>();
        //Restore ArrayList<movie> saved inside onSaveInstanceState during orientation changes
        if(savedInstanceState!=null && savedInstanceState.containsKey("popular_movie")) {
            movies = (ArrayList<Movie>) savedInstanceState.getSerializable("popular_movie");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popular_frag,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Setting up Toolbar title
        Toolbar tbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar_title = (TextView) tbar.findViewById(R.id.toolbar_txt);
        toolbar_title.setText(TITLE);
        //Below condition makes sure no API call is made during orientation changes.
        if(movies.size()>0){
            //If else logic to decide which adapter to use..
            if(preferences.getBoolean(FILTER_FAVORITES,false)) {
                favAdapter = new FavAdapter(getActivity(), R.layout.grid_item, movies);
                gridView.setAdapter(favAdapter);
            }
            else {
                adapter = new GridAdapter(getActivity(), R.layout.grid_item, movies);
                gridView.setAdapter(adapter);
            }

            //Checks whether detailFragment is present(TABLET LAYOUT LANDSCAPE MODE) or not
            // and accordingly set content of this layout to show detail of first element in corresponding gridview in left fragment
            if(detailFragment!=null){
                dfrag = new DetailFragment();
                Bundle b = new Bundle();
                Movie movie = movies.get(0);
                b.putParcelable("MOVIE",movie);
                if (preferences.getBoolean(FILTER_FAVORITES, false))
                    b.putBoolean("IMAGE_LOCAL", true);
                else
                    b.putBoolean("IMAGE_LOCAL", false);
                dfrag.setArguments(b);
                getChildFragmentManager().beginTransaction().replace(R.id.detail_fragment,dfrag).addToBackStack(null).commit();
            }
        }
        else{
            CheckConnectionAndFetch();
        }

        /**
         * Either opens up new detail activity or shows detail inside detail fragment on right side
         * depending on the screen on which the layout is getting viewed
         *
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /**
                 * ImageView is used as shared Element in transition
                 * Check if it is TABLET in LANDSCAPE mode by checking whether detailFragment is present in xml or not
                 */

                if(detailFragment==null) {
                    ImageView iv = (ImageView) view.findViewById(R.id.picture);
                    android.support.v4.util.Pair<View, String> p1 = android.support.v4.util.Pair.create((View) iv, "POSTER");
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1);
                    Intent i = new Intent(getActivity(), DetailActivity.class);
                    Movie movie = movies.get(position);
                    Bundle b = new Bundle();
                    b.putParcelable("MOVIE", movie);//send movie object as bundle to be used in detail activity screen.
                    if (preferences.getBoolean(FILTER_FAVORITES, false)){
                        b.putBoolean("IMAGE_LOCAL", true);//send boolean 'true' to tell detail activity screen to load images from local storage
                    }
                    else {
                        b.putBoolean("IMAGE_LOCAL", false);//send boolean 'false' to tell detail  activity screen to load images from url..
                    }
                    i.putExtras(b);
                    getActivity().startActivity(i,optionsCompat.toBundle());
                }
                //Setup to make detail fragment show the detail about first element of currently displayed list/grid.
                else{
                    dfrag = new DetailFragment();
                    Bundle b = new Bundle();
                    Movie movie = movies.get(position);
                    b.putParcelable("MOVIE",movie);
                    if (preferences.getBoolean(FILTER_FAVORITES, false))
                        b.putBoolean("IMAGE_LOCAL", true);
                    else
                        b.putBoolean("IMAGE_LOCAL", false);
                    dfrag.setArguments(b);
                    getChildFragmentManager().beginTransaction().replace(R.id.detail_fragment,dfrag).addToBackStack(null).commit();
                }
            }
        });

        //Handles retry of fetching data on no-internet connection condition
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_internet.setVisibility(View.GONE);
                CheckConnectionAndFetch();

            }
        });

        //Handles retry on reception of volley error message either due to timeout/network error/volley error
        retry_volley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_volley.setVisibility(View.GONE);
                CheckConnectionAndFetch();
            }
        });

        /**
         * This shows up when user selects Favorites filter and no movie is present in favorite database.
         * So,we provide user with option to start browsing movies so he/she could start adding movies to FAV collection
         */
        browse_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_fav_added.setVisibility(View.GONE);
                editor = preferences.edit();
                editor.clear();
                editor.apply();
                toolbar_title.setText(TITLE);
                if(menu_item!=null)
                    menu_item.setIcon(R.drawable.filter_icon);
                CheckConnectionAndFetch();
            }
        });

    }

    //Cancels all pending API calls
    @Override
    public void onPause() {
        super.onPause();
        AppController.getInstance().cancelPendingRequests(TAG);
    }


    @Override
    public void onResume() {
        super.onResume();
        //This is done to ensure that we get latest details from favorites table when returning to this fragment..
        if(preferences!=null && preferences.getBoolean(FILTER_FAVORITES,false)){
            FetchFavorites();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //This is to make sure right part of dual pane fragment is blank once left part is destroyed..
        if(dfrag!=null){
            getChildFragmentManager().beginTransaction().remove(dfrag).commitAllowingStateLoss();
        }
        ButterKnife.unbind(this);
        bus.unregister(this);//unregister for Delete Event (event listening to removing favorites from the list inside detail fragment)
    }

    //Called when a movie is removed from Fav movie list..
    public void onEvent(DeleteEvent event){
        Log.d("XYZA","On Event Called..");
        FetchFavorites();
    }


    //Checks for Internet Connection prior to making any API requests..
    private void CheckConnectionAndFetch(){
        if(cd.isConnectedToInternet()) {
            if(!(preferences.getBoolean(SORT_POPULAR,false) || preferences.getBoolean(SORT_RATING,false)
                    || preferences.getBoolean(FILTER_FAVORITES,false))){
                FetchData(MOVIES_URL);
            }
            else if (preferences.getBoolean(SORT_POPULAR,false)) {
                FetchData(SORT_BY_POPULAR);
            }
            else if (preferences.getBoolean(SORT_RATING,false)) {
                FetchData(SORT_BY_RATING);
            }
            else if(preferences.getBoolean(FILTER_FAVORITES,false)){
                //skip so that it is picked inside onResume()
            }
        }
        else if(preferences.getBoolean(FILTER_FAVORITES,false)){
             //skip so that it is picked up inside onResume() when no internet connection is present..
        }
        else{
            NoInternetElements();
        }
    }


    //Makes API call using Volley.
    private void FetchData(String URL){
        hideElements();
        movies.clear();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL + getResources().getString(R.string.api_key),null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for(int i=0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Movie member = new Movie();
                        member.setId(jsonObject.getString("id"));
                        member.setTitle(jsonObject.getString("original_title"));
                        member.setPoster(IMAGE_BASE_URL + jsonObject.getString("poster_path"));
                        member.setBackdrop(IMAGE_BASE_URL + jsonObject.getString("backdrop_path"));
                        member.setPlot(jsonObject.getString("overview"));
                        member.setVotes(jsonObject.getString("vote_count"));
                        member.setRating(jsonObject.getDouble("vote_average"));
                        member.setRelease_date(jsonObject.getString("release_date"));
                        movies.add(member);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showElements();
                adapter = new GridAdapter(getActivity(),R.layout.grid_item,movies);
                gridView.setAdapter(adapter);
                if(detailFragment!=null) {
                    dfrag = new DetailFragment();
                    Bundle b = new Bundle();
                    Movie movie = movies.get(0);
                    b.putParcelable("MOVIE", movie);
                    if (preferences.getBoolean(FILTER_FAVORITES, false))
                        b.putBoolean("IMAGE_LOCAL", true);
                    else
                        b.putBoolean("IMAGE_LOCAL", false);
                    dfrag.setArguments(b);
                    getChildFragmentManager().beginTransaction().replace(R.id.detail_fragment, dfrag).addToBackStack(null).commit();
                }

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
               if(error instanceof TimeoutError){
                   RetryVolleyElements("Uh-Oh!! Our Server was very slow to respond..Sorry!\nPlease try again!");
               }
               else if(error instanceof NetworkError){
                   RetryVolleyElements("Uh-Oh!! This network issue pisses me off..\nPlease check network settings and try again!");
               }
               else if(error instanceof VolleyError){
                   RetryVolleyElements("Duh!!Not sure what happened there!\n Please try again!");
               }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq,TAG);
    }

    //Queries database to fetch favorites movies from Content Provider
    private void FetchFavorites(){
        toolbar_title.setText(FAV_TITLE);
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                movies.clear();
                while(cursor.moveToNext()){
                    Movie movie = new Movie();
                    movie.setId(cursor.getString(cursor.getColumnIndex(DbHelper.MOVIE_ID)));
                    movie.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.MOVIE_TITLE)));
                    movie.setPlot(cursor.getString(cursor.getColumnIndex(DbHelper.MOVIE_PLOT)));
                    movie.setPoster(cursor.getString(cursor.getColumnIndex(DbHelper.MOVIE_IMG_URL)));
                    movie.setVotes(cursor.getString(cursor.getColumnIndex(DbHelper.MOVIE_VOTES)));
                    movie.setRelease_date(cursor.getString(cursor.getColumnIndex(DbHelper.MOVIE_REL_DATE)));
                    movie.setRating(cursor.getDouble(cursor.getColumnIndex(DbHelper.MOVIE_RATING)));
                    movies.add(movie);
                }
                pbar.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                favAdapter = new FavAdapter(getActivity(),R.layout.grid_item,movies);
                gridView.setAdapter(favAdapter);
                if(movies.size()>0) {
                    //only showing detail fragment if there are favorite movies present in database..
                    if (detailFragment != null) {
                        dfrag = new DetailFragment();
                        Bundle b = new Bundle();
                        Movie movie = movies.get(0);
                        b.putParcelable("MOVIE", movie);
                        if (preferences.getBoolean(FILTER_FAVORITES, false))
                            b.putBoolean("IMAGE_LOCAL", true);
                        else
                            b.putBoolean("IMAGE_LOCAL", false);
                        dfrag.setArguments(b);
                        getChildFragmentManager().beginTransaction().replace(R.id.detail_fragment, dfrag).addToBackStack(null).commit();
                    }
                }
                else{
                    NoFavElements();
                    //When there are no favorites empty your detail_fragment stack...
                    if(detailFragment!=null && dfrag!=null){
                        getChildFragmentManager().beginTransaction().remove(dfrag).commitAllowingStateLoss();
                    }
                }


            }
        };
        //Make AsyncQueryHandler Call to Fetch Fav Movies from the Database..
        queryHandler.startQuery(1,null,DataProvider.FAV_PROVIDER_URI,null,null,null,null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu_item = menu.findItem(R.id.action_filter);
        if(preferences.getBoolean(SORT_POPULAR,false) || preferences.getBoolean(SORT_RATING,false)
           || preferences.getBoolean(FILTER_FAVORITES,false)) {
            menu_item.setIcon(R.drawable.filter_icon_selected);
        }
        else {
            menu_item.setIcon(R.drawable.filter_icon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_filter:
                Intent filter = new Intent(getActivity(),FilterActivity.class);
                startActivityForResult(filter,INTENT_REQ);
                break;
            case R.id.action_search:
                Intent search = new Intent(getActivity(),SearchActivity.class);
                startActivity(search);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INTENT_REQ){
            if(resultCode == Activity.RESULT_OK){
                if(rl_internet.getVisibility() == View.VISIBLE)
                    rl_internet.setVisibility(View.INVISIBLE);
                if(rl_volley.getVisibility() == View.VISIBLE)
                    rl_volley.setVisibility(View.INVISIBLE);
                if(no_fav_added.getVisibility() == View.VISIBLE)
                    no_fav_added.setVisibility(View.INVISIBLE);
                getActivity().invalidateOptionsMenu();

                if(dfrag!=null){
                    getChildFragmentManager().beginTransaction().remove(dfrag).commitAllowingStateLoss();
                }

                String sort_param = data.getStringExtra("SORT");
                if(sort_param.equals("POPULARITY")){
                    toolbar_title.setText(TITLE);
                    FetchData(SORT_BY_POPULAR);
                    if(menu_item!=null)
                    menu_item.setIcon(R.drawable.filter_icon_selected);
                }
                else if(sort_param.equals("RATING")){
                    toolbar_title.setText(TITLE);
                    FetchData(SORT_BY_RATING);
                    if(menu_item!=null)
                    menu_item.setIcon(R.drawable.filter_icon_selected);
                }
                else if(sort_param.equals("FAV")){
                    if(!bus.isRegistered(this))
                        bus.register(this);
                    FetchFavorites();
                    if(menu_item!=null)
                      menu_item.setIcon(R.drawable.filter_icon_selected);
                }
                else if(sort_param.equals("RESET")){
                    toolbar_title.setText(TITLE);
                    FetchData(MOVIES_URL);
                    if(menu_item!=null)
                    menu_item.setIcon(R.drawable.filter_icon);
                }

            }
        }

    }

    //Store ArrayList<Movie> movies to be restored during Orientation Changes.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("popular_movie",movies);
    }


    //Hides Gridview and shows ProgressBar while API Call is going on
    private void hideElements(){
        gridView.setVisibility(View.INVISIBLE);
        pbar.setVisibility(View.VISIBLE);
    }

    //Brings back Gridview and hides ProgressBar once API call is complete.
    private void showElements(){
        gridView.setVisibility(View.VISIBLE);
        pbar.setVisibility(View.INVISIBLE);
    }

    private void NoInternetElements(){
        rl_internet.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);
        pbar.setVisibility(View.INVISIBLE);

    }

    private void RetryVolleyElements(String error){
        rl_volley.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);
        pbar.setVisibility(View.INVISIBLE);
        volley_error_msg.setText(error);
    }

    private void NoFavElements(){
        no_fav_added.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);
        pbar.setVisibility(View.INVISIBLE);
    }

}
