package com.example.punit.popularmovies.Helpers;

/**
 * DeleteEvent created to be used for communicating Favorite deletion activity from DetailFragment class..
 */

public class DeleteEvent {
    private boolean fav_deleted;

    public DeleteEvent(boolean fav_deleted){
        this.fav_deleted = fav_deleted;
    }

    public boolean isFav_deleted(){
        return this.fav_deleted;
    }
}
