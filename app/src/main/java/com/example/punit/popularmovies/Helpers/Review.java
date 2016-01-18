package com.example.punit.popularmovies.Helpers;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Review implements Parcelable,Serializable{
  private String review;
  private String reviewer;

  public Review(){

  }

  public String getReview() {
        return review;
    }

  public void setReview(String review) {
        this.review = review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
           parcel.writeString(getReviewer());
           parcel.writeString(getReview());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Review(Parcel in) {
       reviewer = in.readString();
       review = in.readString();

    }
}

