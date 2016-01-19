package com.example.punit.popularmovies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punit.popularmovies.Database.DbHelper;
import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Used inside PopularMoviesFragment to populate Favorite movies in Gridview..
 */

public class FavAdapter extends ArrayAdapter<String> {

    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private Context mContext;

    public FavAdapter(Context context, int resource,ArrayList<Movie> movies) {
        super(context, resource);
        mContext = context;
        this.movies = movies;
    }

    @Override
    public int getCount(){
        return movies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.poster = (ImageView) convertView.findViewById(R.id.picture);
            viewHolder.movie_title = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext)
                .load(new File(movies.get(position).getPoster()))
                .into(viewHolder.poster);

        viewHolder.movie_title.setText(movies.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder{
        public ImageView poster;
        public TextView movie_title;
    }
}


