package com.example.punit.popularmovies.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.punit.popularmovies.Adapters.ReviewAdapter;
import com.example.punit.popularmovies.Helpers.Review;
import com.example.punit.popularmovies.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity to display all reviews of particular movie..
 *
 */
public class All_Reviews_Activity extends AppCompatActivity {
    /**
     * Views Initialization using ButterKnife plus other instance variables..
     */
    @Bind(R.id.all_reviews_list) RecyclerView rv;
    @Bind(R.id.toolbar) Toolbar tbar;
    @Bind(R.id.toolbar_txt) TextView toolbar_title;
    ArrayList<Review> reviews;
    ReviewAdapter adapter;
    private static final String SAVE_INSTANCE = "REVIEWS";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_reviews_activity);
        ButterKnife.bind(this);
        //Setting up toolbar
        setSupportActionBar(tbar);
        toolbar_title.setText("REVIEWS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Setting up RecyclerView..
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(All_Reviews_Activity.this);
        rv.setLayoutManager(llm);

        //Handling Orientation Changes...
        if(savedInstanceState!=null){
            reviews = (ArrayList<Review>) savedInstanceState.getSerializable(SAVE_INSTANCE);
        }
        else {
            reviews = new ArrayList<Review>();
            reviews = getIntent().getParcelableArrayListExtra("REVIEWS");
        }

       //Setting up Adapter..
        adapter = new ReviewAdapter(All_Reviews_Activity.this,reviews);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handling Home button Click
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Saving Reviews ArrayList to avoid re-querying during Orientation changes..
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_INSTANCE,reviews);
    }
}
