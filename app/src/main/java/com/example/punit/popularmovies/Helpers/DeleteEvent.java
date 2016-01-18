package com.example.punit.popularmovies.Helpers;

public class DeleteEvent {
    private boolean fav_deleted;

    public DeleteEvent(boolean fav_deleted){
        this.fav_deleted = fav_deleted;
    }

    public boolean isFav_deleted(){
        return this.fav_deleted;
    }
}
