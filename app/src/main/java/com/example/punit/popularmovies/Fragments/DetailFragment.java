package com.example.punit.popularmovies.Fragments;

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
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punit.popularmovies.Activities.TrailersActivity;
import com.example.punit.popularmovies.Database.DataProvider;
import com.example.punit.popularmovies.Database.DbHelper;
import com.example.punit.popularmovies.Helpers.DeleteEvent;
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

public class DetailFragment extends Fragment {

    @Bind(R.id.toolbar) Toolbar tbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.backdrop) ImageView backdrop;
    @Bind(R.id.plot) TextView plot;
    @Bind(R.id.rating) TextView ratings;
    @Bind(R.id.votes) TextView votes;
    @Bind(R.id.release_date) TextView release_date;
    @Nullable @Bind(R.id.trailer_fab) FloatingActionButton fab;
    @Nullable @Bind(R.id.landscape_trailer_btn) Button land_trailer_btn;
    Movie movie;
    SimpleDateFormat format1,format2;
    Date date;
    String dateString;
    boolean is_favorite;
    MenuItem menuItem;
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movie = getArguments().getParcelable("MOVIE");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_frag,container,false);
        ButterKnife.bind(this,v);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        format1 = new SimpleDateFormat("yyyy-MM-dd");
        format2 = new SimpleDateFormat("dd-MMM-yyyy");

        tbar.inflateMenu(R.menu.detail_menu);
        menuItem = tbar.getMenu().findItem(R.id.action_fav);

        //Favorite Button logic
        Cursor c = getParentFragment().getActivity().getContentResolver().query(DataProvider.FAV_PROVIDER_URI,null, DbHelper.MOVIE_ID + " = ?",new String[]{movie.getId()},null);
        if(c.getCount()>0){
            Log.d("FAV","is_favorite is set to true");
            menuItem.setIcon(R.drawable.fav_red_icon);
            is_favorite = true;
        }
        else{
            Log.d("FAV","is_favorite is set to false");
            menuItem.setIcon(R.drawable.fav_icon);
            is_favorite = false;
        }
        if(c!=null && !c.isClosed())
            c.close();


        collapsingToolbarLayout.setTitle(movie.getTitle());
        release_date.setText(DateConversion(movie.getRelease_date()));
        loadBackdrop();
        plot.setText(movie.getPlot());
        ratings.setText(String.valueOf((int)(movie.getRating()*10) + "%"));//converting rating to percentage format.
        votes.setText(movie.getVotes() + " votes");
        if(fab!=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(),TrailersActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("MOVIE",movie);
                    i.putExtras(b);
                    startActivity(i);
                }
            });

        if(land_trailer_btn!=null)
            land_trailer_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(),TrailersActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("MOVIE",movie);
                    i.putExtras(b);
                    startActivity(i);
                }
            });

        if(tbar!=null){
            tbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.action_fav:
                            if(is_favorite){
                                Log.d("FAV","WE ARE DELETING FAV BRO!!");
                                //delete
                                final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
                                    @Override
                                    protected void onDeleteComplete(int token, Object cookie, int result) {
                                        super.onDeleteComplete(token, cookie, result);
                                        menuItem.setIcon(R.drawable.fav_icon);
                                        is_favorite = false;
                                        DeleteEvent event = new DeleteEvent(true);
                                        bus.post(event);
                                        Log.d("XYZA","We have deleted favorite");
                                    }
                                };
                                asyncQueryHandler.startDelete(1,null, DataProvider.FAV_PROVIDER_URI, DbHelper.MOVIE_ID + " = ?",new String[]{movie.getId()});

                            }
                            else{

                                Log.d("FAV","WE ARE INSERTING FAV BRO!!");
                                final ContentValues cv = new ContentValues();
                                cv.put(DbHelper.MOVIE_ID, movie.getId());
                                cv.put(DbHelper.MOVIE_TITLE, movie.getTitle());
                                cv.put(DbHelper.MOVIE_PLOT, movie.getPlot());
                                cv.put(DbHelper.MOVIE_VOTES, movie.getVotes());
                                cv.put(DbHelper.MOVIE_REL_DATE, movie.getRelease_date());
                                cv.put(DbHelper.MOVIE_RATING, movie.getRating());
                                final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
                                    @Override
                                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                                        super.onInsertComplete(token, cookie, uri);
                                        menuItem.setIcon(R.drawable.fav_red_icon);
                                        is_favorite = true;
                                    }
                                };
                                if(!CheckIfImageExists()) {
                                    Log.d("TAG","Downloading");
                                    Picasso.with(getActivity())
                                            .load(movie.getPoster())
                                            .into(new Target() {
                                                @Override
                                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                    //saveToInternalStorage(bitmap);
                                                    cv.put(DbHelper.MOVIE_IMG_URL, saveToInternalStorage(bitmap));
                                                    //Log.d("TAG", saveToInternalStorage(bitmap));
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
                                else{
                                    Log.d("TAG","Not Downloading");
                                    cv.put(DbHelper.MOVIE_IMG_URL, ExistingImagePath());
                                    Log.d("TAG", ExistingImagePath());
                                    asyncQueryHandler.startInsert(2,null,DataProvider.FAV_PROVIDER_URI,cv);
                                }
                            }
                             break;
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

                    }
                    return false;
                }
            });
        }
    }


    private void loadBackdrop(){
        if(getArguments().getBoolean("IMAGE_LOCAL")){
            Log.d("STACK", "We are loading images from local storage");
            Log.d("STACK",movie.getPoster());
            Picasso.with(getActivity()).load(new File(movie.getPoster())).into(backdrop);
        }
        else {
            Picasso.with(getActivity()).load(movie.getPoster()).into(backdrop);
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

    private String saveToInternalStorage(Bitmap bitmap){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
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

    private boolean CheckIfImageExists(){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File dir = cw.getDir("Favorites", Context.MODE_PRIVATE);

        File path = new File(dir,movie.getId() + ".jpg");
        if(path.exists())
            return true;
        else
            return false;
    }

    private String ExistingImagePath(){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File dir = cw.getDir("Favorites", Context.MODE_PRIVATE);

        File path = new File(dir,movie.getId() + ".jpg");
        if(path.exists())
            return path.getAbsolutePath();
        else
            return null;
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
        menuItem = menu.findItem(R.id.action_fav);
        Cursor c = getActivity().getContentResolver().query(DataProvider.FAV_PROVIDER_URI,null, DbHelper.MOVIE_ID + " = ?",new String[]{movie.getId()},null);
        if(c.getCount()>0){
            Log.d("FAV","is_favorite is set to true");
            menuItem.setIcon(R.drawable.fav_red_icon);
            is_favorite = true;
        }
        else{
            Log.d("FAV","is_favorite is set to false");
            menuItem.setIcon(R.drawable.fav_icon);
            is_favorite = false;
        }
        if(c!=null && !c.isClosed())
            c.close();

    }
    */
}
