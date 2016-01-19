package com.example.punit.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.punit.popularmovies.Helpers.Review;
import com.example.punit.popularmovies.R;

import java.util.ArrayList;

/**
 * Used by All_Reviews_Activity to display all reviews for particular movie using recyclerviews..
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.RVHolder> {

    private Context mContext;
    private ArrayList<Review> reviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews){
        mContext = context;
        this.reviews = reviews;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.review_row,parent,false);
        RVHolder rvHolder = new RVHolder(v);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(RVHolder holder, int position) {
        holder.reviewer.setText(reviews.get(position).getReviewer());
        holder.review.setText(reviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class RVHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView reviewer;
        TextView review;
        public RVHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardview);
            reviewer = (TextView) itemView.findViewById(R.id.reviewer);
            review = (TextView) itemView.findViewById(R.id.review_txt);
        }


    }

}
