package com.example.punit.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Favorites.db";
    private static final int DATABASE_VERSION = 1;

    public static final String FAV_TABLE_NAME = "Favorites";
    public static final String _ID = "_id";
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_IMG_URL = "image_url";
    public static final String MOVIE_REL_DATE = "release_date";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_VOTES = "votes";
    public static final String MOVIE_RATING = "rating";

    private static final String FAV_TABLE_CREATE = "CREATE TABLE " + FAV_TABLE_NAME
                  + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MOVIE_ID + " TEXT, " + MOVIE_TITLE + " TEXT,"
                 + MOVIE_IMG_URL + " TEXT," + MOVIE_REL_DATE + " TEXT," + MOVIE_PLOT + " TEXT,"
                 + MOVIE_VOTES  + " TEXT," + MOVIE_RATING + " REAL);";

    private static final String FAV_TABLE_DROP = "DROP TABLE IF EXISTS " + FAV_TABLE_NAME;


    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAV_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(FAV_TABLE_DROP);
        db.execSQL(FAV_TABLE_CREATE);
    }
}
