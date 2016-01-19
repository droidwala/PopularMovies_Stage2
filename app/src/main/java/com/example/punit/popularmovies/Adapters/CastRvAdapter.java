package com.example.punit.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punit.popularmovies.Helpers.Cast;
import com.example.punit.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Used by CastFragment to display Cast details of the movie inside DetailActivity in RecyclerView..
 */
public class CastRvAdapter extends RecyclerView.Adapter<CastRvAdapter.RVHolder>{

    private Context mContext;
    private ArrayList<Cast> casts;

    public CastRvAdapter(Context context,ArrayList<Cast> casts){
        mContext = context;
        this.casts = casts;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_cast_details,parent,false);
        RVHolder holder = new RVHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RVHolder holder, int position) {
        holder.cast_name.setText(casts.get(position).getCast_name());
        Picasso.with(mContext)
               .load(casts.get(position).getCast_image())
               .placeholder(R.drawable.placeholder_icon)
               .into(holder.cast_image);

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class RVHolder extends RecyclerView.ViewHolder{

        ImageView cast_image;
        TextView cast_name;

        public RVHolder(View itemView) {
            super(itemView);
            cast_image = (ImageView) itemView.findViewById(R.id.cast_image);
            cast_name = (TextView) itemView.findViewById(R.id.cast_name);
        }
    }
}

