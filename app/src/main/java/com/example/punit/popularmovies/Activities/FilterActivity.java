package com.example.punit.popularmovies.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.punit.popularmovies.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity to display filter choices like Popular/Highly Rated/Favorites to user to update main fragment(PopularFragment)..
 */


public class FilterActivity extends AppCompatActivity {

    //Views Initialization by ButterKnife..
    @Bind(R.id.toolbar) Toolbar tbar;
    @Bind(R.id.toolbar_txt) TextView tbar_title;
    @Bind(R.id.apply_filters) Button apply_filter;
    @Bind(R.id.sort_group) RadioGroup rg;
    @Bind(R.id.popularity_sort) RadioButton popularity_sort;
    @Bind(R.id.rate_sort) RadioButton rating_sort;
    @Bind(R.id.fav_movies) CheckBox fav_movies;
    //Instance variables..
    MenuItem reset;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //static variables..
    private static final String PREFS_NAME="SORT_CRITERIA";
    private static final String SORT_POPULAR="POPULAR";
    private static final String SORT_RATING ="RATING";
    private static final String FILTER_FAVORITES = "FAVORITES";
    private static final String TITLE="FILTER";
    private static final String SAVE_INSTANCE ="APPLY_FILTER_BTN";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        //setting up toolbar title..
        setSupportActionBar(tbar);
        tbar_title.setText(TITLE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_clear_mtrl_alpha);

        //Restoring Apply Filter Button's visibility on Orientation changes..
        if(savedInstanceState!=null){
            if(savedInstanceState.getBoolean(SAVE_INSTANCE)){
                apply_filter.setVisibility(View.VISIBLE);
            }
        }

        //setting up radio button and apply filter button depending on preference values
        preferences = getSharedPreferences(PREFS_NAME,0);
        popularity_sort.setChecked(preferences.getBoolean(SORT_POPULAR,false));
        rating_sort.setChecked(preferences.getBoolean(SORT_RATING,false));
        fav_movies.setChecked(preferences.getBoolean(FILTER_FAVORITES,false));

        //Apply filter shown only if either of three is checked..
        if(popularity_sort.isChecked() || rating_sort.isChecked() || fav_movies.isChecked()){
            apply_filter.setVisibility(View.VISIBLE);
        }

        /**
         * Handling Radio buttons and Checkbox Clicks..
         */
        popularity_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popularity_sort.isChecked()){
                    if(fav_movies.isChecked()) {
                        fav_movies.setChecked(false);
                    }
                    apply_filter.setVisibility(View.VISIBLE);
                    reset.setVisible(true);
                }
            }
        });

        rating_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rating_sort.isChecked()){
                    if(fav_movies.isChecked())
                        fav_movies.setChecked(false);
                    apply_filter.setVisibility(View.VISIBLE);
                    reset.setVisible(true);
                }
            }
        });

        fav_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav_movies.isChecked()){
                    Log.d("CHECK","Checkbox is checked");
                    if(popularity_sort.isChecked() || rating_sort.isChecked())
                        rg.clearCheck();
                    reset.setVisible(true);
                    apply_filter.setVisibility(View.VISIBLE);
                }
                else{
                    reset.setVisible(false);
                    apply_filter.setVisibility(View.INVISIBLE);
                    Log.d("CHECK","Checkbox is unchecked");
                }
            }
        });

        //Apply Filter button click handling
        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
               // if popularity is checked then save the same in preferences and send returnIntent with Sort details as "POPULARITY" to MainActivity
               if(popularity_sort.isChecked()){
                   editor = preferences.edit();
                   editor.putBoolean(SORT_RATING,false);
                   editor.putBoolean(FILTER_FAVORITES,false);
                   editor.putBoolean(SORT_POPULAR,true);
                   editor.apply();
                   returnIntent.putExtra("SORT","POPULARITY");
                   setResult(Activity.RESULT_OK,returnIntent);
                   finish();
               }
               //similarly for Rating radio button
               else if (rating_sort.isChecked()){
                   editor = preferences.edit();
                   editor.putBoolean(SORT_POPULAR,false);
                   editor.putBoolean(FILTER_FAVORITES,false);
                   editor.putBoolean(SORT_RATING,true);
                   editor.apply();
                   returnIntent.putExtra("SORT","RATING");
                   setResult(Activity.RESULT_OK,returnIntent);
                   finish();
               }
               //Similarly for Favorite Movies..
                else if(fav_movies.isChecked()){
                   editor = preferences.edit();
                   editor.putBoolean(SORT_POPULAR,false);
                   editor.putBoolean(SORT_RATING,false);
                   editor.putBoolean(FILTER_FAVORITES,true);
                   editor.apply();
                   returnIntent.putExtra("SORT","FAV");
                   setResult(Activity.RESULT_OK,returnIntent);
                   finish();
               }

            }
        });
    }


    //Inflates menu and shows reset menu item depending whether radio button's are checked or not.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reset_sort,menu);
        reset = menu.findItem(R.id.reset);
        if(!(popularity_sort.isChecked()||rating_sort.isChecked() || fav_movies.isChecked())){
              reset.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    //Menu items click handling
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //Home button click handling
            case android.R.id.home:
                finish();
                break;
            //Reset Menu button click handling
            case R.id.reset:
                //Clear state of radio buttons and checkbox on reset click
                rg.clearCheck();
                fav_movies.setChecked(false);

                //make reset and apply filter button invisible
                reset.setVisible(false);
                apply_filter.setVisibility(View.INVISIBLE);

                //If neither of them are checked and saved in preferences we don't send return Intent
                if(!(preferences.getBoolean(SORT_POPULAR,false) || preferences.getBoolean(SORT_RATING,false) ||
                     preferences.getBoolean(FILTER_FAVORITES,false))){
                    finish();
                }
                //clear out all preferences once reset is clicked.
                else{
                    editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent i = new Intent();
                    i.putExtra("SORT","RESET");
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Saving Apply Filter Button's visibility state during orientation changes..
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_INSTANCE,(apply_filter.getVisibility()==View.VISIBLE));
    }
}
