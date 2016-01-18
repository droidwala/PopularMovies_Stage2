package com.example.punit.popularmovies.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.punit.popularmovies.Activities.All_Reviews_Activity;
import com.example.punit.popularmovies.Application.AppController;
import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.Helpers.Review;
import com.example.punit.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewFragment extends Fragment implements View.OnClickListener{


    @Bind(R.id.progressBar) ProgressBar pbar;
    @Bind(R.id.first_reviewer) TextView first_reviewer;
    @Bind(R.id.first_review) TextView first_review;
    @Bind(R.id.read_all_reviews) TextView read_all_reviews;
    @Bind(R.id.no_reviews) TextView no_review_msg;
    private static String REVIEW_START_URL = "http://api.themoviedb.org/3/movie/";
    private static String REVIEW_END_URL = "/reviews?api_key=";
    private ArrayList<Review> reviews;
    private static final String TAG = "REVIEW";
    private static final String SAVE_INSTANCE = "REVIEW";
    Movie movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviews = new ArrayList<Review>();
        if(savedInstanceState!=null){
            reviews = (ArrayList<Review>) savedInstanceState.getSerializable(SAVE_INSTANCE);
            Log.d("STACK","We don't fetch data again on Orientation Change");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_review, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movie = getActivity().getIntent().getParcelableExtra("MOVIE");
        if(reviews.size()>0){
            Review_Logic();
        }
        else{
            FetchData(movie.getId());
        }

        read_all_reviews.setOnClickListener(this);
        first_review.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void FetchData(String movie_id) {
        Log.d("STACK","Do we fetch reviews again on orientation change");
        hideElements();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                REVIEW_START_URL + movie_id + REVIEW_END_URL + getResources().getString(R.string.api_key),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("results");
                            if (jsonArray.length() > 0) {
                                Log.d("TAG", String.valueOf(jsonArray.length()));
                                reviews.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Review review = new Review();
                                    review.setReview(jsonObject.getString("content"));
                                    review.setReviewer(jsonObject.getString("author"));
                                    reviews.add(review);
                                }

                            }

                            Review_Logic();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbar.setVisibility(View.GONE);
                first_reviewer.setVisibility(View.INVISIBLE);
                first_review.setVisibility(View.VISIBLE);
                read_all_reviews.setVisibility(View.VISIBLE);
                first_review.setText("Sorry,cannot fetch reviews due to network connection issue.Please Check Network Settings and Try Again");
                read_all_reviews.setText("TRY AGAIN");
            }
        });
        AppController.getInstance().addToRequestQueue(request, TAG);
    }


    private void Review_Logic(){
        if(reviews.size()>0){
            showElements();
            first_reviewer.setText(reviews.get(0).getReviewer());
            first_review.setText(reviews.get(0).getReview());
            read_all_reviews.setText("READ ALL REVIEWS");

        }
        else{
            no_review_msg.setVisibility(View.VISIBLE);
            read_all_reviews.setVisibility(View.GONE);
            pbar.setVisibility(View.GONE);
            no_review_msg.setText("Sorry,No Reviews Found!");
        }
    }
    private void hideElements(){
        first_review.setVisibility(View.INVISIBLE);
        first_reviewer.setVisibility(View.INVISIBLE);
        read_all_reviews.setVisibility(View.INVISIBLE);
        pbar.setVisibility(View.VISIBLE);
    }
    private void showElements(){
        first_reviewer.setVisibility(View.VISIBLE);
        first_review.setVisibility(View.VISIBLE);
        read_all_reviews.setVisibility(View.VISIBLE);
        pbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {

        if(first_reviewer.getVisibility()== View.VISIBLE) {
            if (view.getId() == R.id.first_review || view.getId() == R.id.read_all_reviews) {
                Intent i = new Intent(getActivity(), All_Reviews_Activity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("REVIEWS", reviews);
                i.putExtras(b);
                startActivity(i);
            }
        }
        else{
            if(view.getId() == R.id.read_all_reviews){
                FetchData(movie.getId());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_INSTANCE,reviews);
    }
}