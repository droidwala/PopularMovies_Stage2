package com.example.punit.popularmovies.Activities;


import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.punit.popularmovies.Database.DataProvider;
import com.example.punit.popularmovies.Database.DbHelper;
import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Activity to display details like Movie poster,Release Date,Plot,
 * Votes Count,Rating,Reviews,Casts,Trailer,etc.
 */
public class DetailActivity extends AppCompatActivity {

    //Views Initialization using Butterknife..
    @Bind(R.id.toolbar) Toolbar tbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.backdrop) ImageView backdrop;
    @Bind(R.id.plot) TextView plot;
    @Bind(R.id.rating) TextView ratings;
    @Bind(R.id.votes) TextView votes;
    @Bind(R.id.release_date) TextView release_date;
    @Nullable @Bind(R.id.trailer_fab)  FloatingActionButton fab;
    @Nullable @Bind(R.id.landscape_trailer_btn) Button land_trailer_btn;

    //Instance Variables initialization
    Movie movie;
    SimpleDateFormat format1,format2;
    Date date;
    String dateString;
    boolean is_favorite;
    MenuItem menuItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //Date Formatting for Release Date
        format1 = new SimpleDateFormat("yyyy-MM-dd");
        format2 = new SimpleDateFormat("dd-MMM-yyyy");

        //Toolbar setup
        setSupportActionBar(tbar);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Restoring Movie Object during Orientation Changes.
        if(savedInstanceState!=null){
            movie = (Movie) savedInstanceState.getSerializable("Movie");
        }
        else {
            movie = getIntent().getParcelableExtra("MOVIE");
        }

        /**
         * Setting up values of all views using movie object.
         */

        //Setting up CollapsingToolbar title..
        collapsingToolbarLayout.setTitle(movie.getTitle());

        //Setting up Release Date..
        release_date.setText(DateConversion(movie.getRelease_date()));

        //Loading Movie poster using Picassso library..
        loadBackdrop();

        //Setting up Plot text..
        plot.setText(movie.getPlot());

        //Setting up Ratings text..
        ratings.setText(String.valueOf((int)(movie.getRating()*10) + "%"));//converting rating to percentage format.
        votes.setText(movie.getVotes() + " votes");

        //Null check is done as FAB button is not present in landscape layout of Detail Activity..
        if(fab!=null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this,TrailersActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("MOVIE",movie);
                i.putExtras(b);
                startActivity(i);
            }
        });

        //Null check is done as Land Button is not present in portrait layout of Detail Activity...
        if(land_trailer_btn!=null)
        land_trailer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this,TrailersActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("MOVIE",movie);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }


    //Uses boolean variable from Bundle(passed from Main Activity) to decide whether to load images from local storage or from web..
    private void loadBackdrop(){
        if(getIntent().getExtras().getBoolean("IMAGE_LOCAL")){
            Picasso.with(this).load(new File(movie.getPoster())).into(backdrop);
        }
        else {
            Picasso.with(this).load(movie.getPoster()).into(backdrop);
        }
    }


    //converts "yyyy-MM-dd" date format received in json results to "dd-MMM-yyyy"
    private String DateConversion(String release_date){
        try {
            date = format1.parse(release_date);
            dateString = format2.format(date);
            dateString = dateString.replace("-"," ");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //Handling Home Button Click
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
            //Handle Share Button Click to generate Intent to share movie details
            case R.id.action_share:
                StringBuilder sb = new StringBuilder();
                sb.append("Movie Name: " + movie.getTitle() + "\n");
                sb.append("Plot: " + movie.getPlot() + "\n");
                sb.append("Release Date: " + DateConversion(movie.getRelease_date())+ "\n");
                sb.append("Votes Count: " + movie.getVotes() + "\n");
                sb.append("Rating: " + String.valueOf((int) (movie.getRating() * 10) + "%") + "\n");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,sb.toString());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,movie.getTitle());
                sendIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"punitdama@gmail.com"});
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            //Handle Favorite button click to add movies to favorites list in the db backed by content provider..
            case R.id.action_fav:
                if(is_favorite){
                    //Remove movie from favorite list..
                    final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
                        @Override
                        protected void onDeleteComplete(int token, Object cookie, int result) {
                            super.onDeleteComplete(token, cookie, result);
                            menuItem.setIcon(R.drawable.fav_icon);//change to color of fav icon to indicate current status..
                            is_favorite = false;
                          }
                    };
                    asyncQueryHandler.startDelete(1,null,DataProvider.FAV_PROVIDER_URI,DbHelper.MOVIE_ID + " = ?",new String[]{movie.getId()});
                }
                else{
                    //Add movie to favorite list..
                    final ContentValues cv = new ContentValues();
                    cv.put(DbHelper.MOVIE_ID, movie.getId());
                    cv.put(DbHelper.MOVIE_TITLE, movie.getTitle());
                    cv.put(DbHelper.MOVIE_PLOT, movie.getPlot());
                    cv.put(DbHelper.MOVIE_VOTES, movie.getVotes());
                    cv.put(DbHelper.MOVIE_REL_DATE, movie.getRelease_date());
                    cv.put(DbHelper.MOVIE_RATING, movie.getRating());
                    final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
                        @Override
                        protected void onInsertComplete(int token, Object cookie, Uri uri) {
                            super.onInsertComplete(token, cookie, uri);
                            menuItem.setIcon(R.drawable.fav_red_icon);
                            is_favorite = true;
                        }
                    };
                    //Check If Image Exists to avoid re-downloading of image for offline usage..
                    if(!CheckIfImageExists()) {
                        Picasso.with(DetailActivity.this)
                                .load(movie.getPoster())
                                .into(new Target() {
                                    /**
                                     * Once bitmap is completely downloaded and saved in Internal Storage
                                     * we insert complete movie information in db
                                     */
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        cv.put(DbHelper.MOVIE_IMG_URL, saveToInternalStorage(bitmap));
                                        asyncQueryHandler.startInsert(2,null,DataProvider.FAV_PROVIDER_URI,cv);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                    }
                    //If image exists we don't download and save operation instead directly save movie information in db...
                    else{
                        cv.put(DbHelper.MOVIE_IMG_URL, ExistingImagePath());
                        asyncQueryHandler.startInsert(2, null, DataProvider.FAV_PROVIDER_URI, cv);
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    //Used to storage image of movie marked as favorite in internal storage for offline usage..
    private String saveToInternalStorage(Bitmap bitmap){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir = cw.getDir("Favorites", Context.MODE_PRIVATE);
        File path = new File(dir,movie.getId() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path.getAbsolutePath();
    }

    //Used to Check whether images with "movie.getId().jpg" exists in internal storage or not?
    private boolean CheckIfImageExists(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir = cw.getDir("Favorites", Context.MODE_PRIVATE);

        File path = new File(dir,movie.getId() + ".jpg");
        if(path.exists())
             return true;
        else
            return false;
    }

    //Used to fetch complete path of existing image in internal storage..
    private String ExistingImagePath(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir = cw.getDir("Favorites", Context.MODE_PRIVATE);

        File path = new File(dir,movie.getId() + ".jpg");
        if(path.exists())
            return path.getAbsolutePath();
        else
            return null;
    }


    //Menu inflation plus logic to decide fav icon depending on whether the movie has been added to favorite list or not?

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu,menu);
        menuItem = menu.findItem(R.id.action_fav);
        Cursor c = getContentResolver().query(DataProvider.FAV_PROVIDER_URI,null, DbHelper.MOVIE_ID + " = ?",new String[]{movie.getId()},null);
        //if present show red icon indicating the movie belongs to favorite list..
        if(c.getCount()>0){
            menuItem.setIcon(R.drawable.fav_red_icon);
            is_favorite = true;
        }
        //else show normal icon indicating movie doesn't belongs to favorite list..
        else{
            menuItem.setIcon(R.drawable.fav_icon);
            is_favorite = false;
        }
        //At the end we close cursor
        if(c!=null && !c.isClosed()) {
            c.close();
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Save Movie object during orientation changes.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Movie",movie);
    }

}
