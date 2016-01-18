package com.example.punit.popularmovies.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.punit.popularmovies.Adapters.TrailerAdapter;
import com.example.punit.popularmovies.Application.AppController;
import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.Helpers.Video;
import com.example.punit.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrailersActivity extends AppCompatActivity {

    @Bind(R.id.all_trailers_list) RecyclerView rv;
    @Bind(R.id.toolbar) Toolbar tbar;
    @Bind(R.id.toolbar_txt) TextView toolbar_title;
    @Bind(R.id.progressBar) ProgressBar pbar;
    @Bind(R.id.no_internet_msg) TextView no_internet_msg;
    @Bind(R.id.no_trailers) TextView no_trailers_msg;
    @Bind(R.id.try_again_btn) Button try_again;
    private static String MOVIE_TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static String MOVIE_TRAILER_END_URL = "/videos?api_key=";
    private static String THUMBNAIL_URL ="http://img.youtube.com/vi/";
    private static String YOUTUBE_VIDEO_URL="https://www.youtube.com/watch?v=";
    private static final String SAVE_INSTANCE = "VIDEOS";
    private Movie movie;
    ArrayList<Video> videos;
    private static final String TAG="TRAILERS";
    TrailerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        ButterKnife.bind(this);
        setSupportActionBar(tbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("TRAILERS");
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        movie = getIntent().getParcelableExtra("MOVIE");
        if(savedInstanceState!=null){
            videos = (ArrayList<Video>) savedInstanceState.getSerializable(SAVE_INSTANCE);
            Log.d("STACK","Just an another orientation change");
            adapter = new TrailerAdapter(TrailersActivity.this,videos);
            rv.setAdapter(adapter);
        }
        else {
            videos = new ArrayList<Video>();
            FetchData(movie.getId());
            Log.d("STACK","fetching movies from server");
        }
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchData(movie.getId());
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    private void FetchData(String movie_id){
      hideElements();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                MOVIE_TRAILER_BASE_URL + movie_id + MOVIE_TRAILER_END_URL + getResources().getString(R.string.api_key),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Video video = new Video();
                                video.setTitle(jsonObject.getString("name"));
                                video.setImage_url(THUMBNAIL_URL + jsonObject.getString("key") + "/0.jpg");
                                video.setVideo_url(YOUTUBE_VIDEO_URL + jsonObject.getString("key"));
                                videos.add(video);
                            }
                           if(videos.size()>0) {
                               adapter = new TrailerAdapter(TrailersActivity.this, videos);
                               rv.setAdapter(adapter);
                               showElements();
                           }
                            else{
                               pbar.setVisibility(View.GONE);
                               no_trailers_msg.setVisibility(View.VISIBLE);
                           }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rv.setVisibility(View.INVISIBLE);
                pbar.setVisibility(View.INVISIBLE);
                no_internet_msg.setVisibility(View.VISIBLE);
                try_again.setVisibility(View.VISIBLE);
            }
        });

         AppController.getInstance().addToRequestQueue(request,TAG);
    }

    private void hideElements(){
        pbar.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        no_internet_msg.setVisibility(View.INVISIBLE);
        try_again.setVisibility(View.INVISIBLE);
    }
    private void showElements(){
        pbar.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_INSTANCE,videos);
    }
}
