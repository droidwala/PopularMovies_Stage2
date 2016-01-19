package com.example.punit.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punit.popularmovies.Helpers.Video;
import com.example.punit.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Used by TrailersActivity to display trailer of particular movie..
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.RVHolder> {

    private Context mContext;
    private ArrayList<Video> videos;

    public TrailerAdapter(Context context, ArrayList<Video> videos){
        mContext = context;
        this.videos = videos;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trailer_row_view,parent,false);
        RVHolder rvHolder = new RVHolder(v,new RVHolder.ViewHolderClicks() {
            @Override
            public void Click_Handle(View v, int position) {
                /**
                 *If user clicks on Play Video or any where on thumbnail we open the video using action_view intent by passing video of the url..
                 * Whichever App supports ACTION_VIEW as intent-filter would show up as option for user to see the video..
                 * Else if user clicks on Share Icon then url of the video would be shared using the apps supporting required action..
                 */
                     if(v.getId()==R.id.play_video || v.getId() == R.id.thumbnail_img){
                         Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videos.get(position).getVideo_url()));
                         mContext.startActivity(i);
                     }
                     else if(v.getId() == R.id.share_video){
                         Intent share_trailer = new Intent();
                         share_trailer.setAction(Intent.ACTION_SEND);
                         share_trailer.putExtra(Intent.EXTRA_TEXT,videos.get(position).getVideo_url());
                         share_trailer.putExtra(Intent.EXTRA_SUBJECT,videos.get(position).getTitle());
                         share_trailer.putExtra(Intent.EXTRA_EMAIL,new String[]{"punitdama@gmail.com"});
                         share_trailer.setType("text/plain");
                         mContext.startActivity(share_trailer);
                     }
            }
        });

        return rvHolder;
    }

    @Override
    public void onBindViewHolder(RVHolder holder, int position) {
        holder.video_title.setText(videos.get(position).getTitle());
        Picasso.with(mContext)
               .load(videos.get(position).getImage_url())
               .into(holder.video_image);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    //Implement click handling using ViewHolderClicks Interface..
    public static class RVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView video_image;
        ImageView play_video;
        ImageView share_video;
        TextView video_title;
        ViewHolderClicks mListener;

        public RVHolder(View itemView,ViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            video_image = (ImageView) itemView.findViewById(R.id.thumbnail_img);
            play_video = (ImageView) itemView.findViewById(R.id.play_video);
            share_video = (ImageView) itemView.findViewById(R.id.share_video);
            video_title = (TextView) itemView.findViewById(R.id.video_title);
            video_image.setOnClickListener(this);
            play_video.setOnClickListener(this);
            share_video.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           //Adapter position is passed along with view..
           mListener.Click_Handle(view,getAdapterPosition());
        }

        //Used for handling clicks..
        public static interface ViewHolderClicks{
            public void Click_Handle(View v,int position);
        }
    }
}
