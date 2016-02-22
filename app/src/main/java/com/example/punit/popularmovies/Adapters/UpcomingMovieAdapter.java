package com.example.punit.popularmovies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punit.popularmovies.Helpers.Movie;
import com.example.punit.popularmovies.Helpers.UpcomingMovies;
import com.example.punit.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpcomingMovieAdapter extends ArrayAdapter<String> {

    private UpcomingMovies upcomingMovies;
    private Context mContext;
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";

    public UpcomingMovieAdapter(Context context, int resource,UpcomingMovies upcomingMovies) {
        super(context, resource);
        mContext = context;
        this.upcomingMovies = upcomingMovies;
    }


    @Override
    public int getCount(){
        return upcomingMovies.getResults().size();
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
                .load(IMAGE_BASE_URL + upcomingMovies.getResults().get(position).getPoster_path())
                .placeholder(R.drawable.pholder)
                .into(viewHolder.poster);

        viewHolder.movie_title.setText(upcomingMovies.getResults().get(position).getTitle());
        return convertView;
    }

    static class ViewHolder{
        public ImageView poster;
        public TextView movie_title;
    }
}