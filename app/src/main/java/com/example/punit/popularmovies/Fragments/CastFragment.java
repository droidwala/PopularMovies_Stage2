package com.example.punit.popularmovies.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.punit.popularmovies.Adapters.CastRvAdapter;
import com.example.punit.popularmovies.Application.AppController;
import com.example.punit.popularmovies.Helpers.Cast;
import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This fragment is used to display casts details inside DetailActivity
 */
public class CastFragment extends Fragment {
    //Views initialization using ButterKnife..
    @Bind(R.id.rv) RecyclerView rv;
    @Bind(R.id.progressBar) ProgressBar pbar;

    //static variables
    private static String BASE_START_URL = "http://api.themoviedb.org/3/movie/";
    private static String BASE_END_URL="/credits?api_key=";
    private static String REQUEST_TAG ="CAST";
    private static String IMAGE_BASE_PATH ="http://image.tmdb.org/t/p/w342";
    private static ArrayList<Cast> casts;

    //instance variables
    CastRvAdapter adapter;
    Movie movie;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        casts = new ArrayList<Cast>();
        //Fetch casts arraylist in case of orientation change to avoid re-fetching of data..
        if(savedInstanceState!=null){
            casts = (ArrayList<Cast>) savedInstanceState.getSerializable("CAST");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_cast,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movie = getActivity().getIntent().getParcelableExtra("MOVIE");
        //During Orientation change we don't re-fetch data else we make API call to fetch casts detail..
        if(casts.size()>0){
            adapter = new CastRvAdapter(getActivity(),casts);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
            rv.setAdapter(adapter);
        }
        else {
            FetchData();
        }
    }

    //Cancel all pending API requests
    @Override
    public void onPause() {
        super.onPause();
        AppController.getInstance().cancelPendingRequests(REQUEST_TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void FetchData(){
        hideElements();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_START_URL + movie.getId() + BASE_END_URL + getResources().getString(R.string.api_key),null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("cast");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setCast_name(jsonObject.getString("name"));
                        cast.setCast_image(IMAGE_BASE_PATH + jsonObject.getString("profile_path"));
                        casts.add(cast);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showElements();
                adapter = new CastRvAdapter(getActivity(),casts);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setHasFixedSize(true);
                rv.setLayoutManager(llm);
                rv.setAdapter(adapter);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(request,REQUEST_TAG);

    }

    private void hideElements(){
        pbar.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
    }
    private void showElements(){
        pbar.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.VISIBLE);
    }

    //Saving casts arraylist during orientation change..
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CAST",casts);
    }
}
