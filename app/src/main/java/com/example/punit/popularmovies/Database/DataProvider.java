package com.example.punit.popularmovies.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Content Provider implementation to provide abstraction for Favorites Database used for storing favorite movies of the user..
 */

public class DataProvider extends ContentProvider {

    DbHelper dbHelper;

    //URI for Favorites table
    public static final Uri FAV_PROVIDER_URI = Uri.parse("content://com.example.punit.popularmovies.provider/data");
    private static final int DATA_ALL_ROWS = 1;
    private static final int DATA_SINGLE_ROW = 2;
    private static final UriMatcher urimatcher;

    //static initializer
    static {
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI("com.example.punit.popularmovies.provider","data",DATA_ALL_ROWS);
        urimatcher.addURI("com.example.punit.popularmovies.provider","data/#",DATA_SINGLE_ROW);

    };

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    //Query method from Content Resolver calls this method..
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (urimatcher.match(uri)){
            case DATA_ALL_ROWS:
                qb.setTables(DbHelper.FAV_TABLE_NAME);
                break;
            case DATA_SINGLE_ROW:
                qb.setTables(DbHelper.FAV_TABLE_NAME);
                qb.appendWhere(DbHelper.MOVIE_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw  new IllegalArgumentException();
        }
        Cursor c = qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    //Insert method from Content Resolver calls this method.
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch(urimatcher.match(uri)){
            case DATA_ALL_ROWS:
                id  = db.insertOrThrow(DbHelper.FAV_TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException();
        }
        Uri insertUri = ContentUris.withAppendedId(uri,id);
        getContext().getContentResolver().notifyChange(insertUri,null);
        return insertUri;

    }

    //Delete method from Content Resolver call this method
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (urimatcher.match(uri)){
            case DATA_ALL_ROWS:
                count = db.delete(DbHelper.FAV_TABLE_NAME,selection,selectionArgs);
                break;
            case DATA_SINGLE_ROW:
                count = db.delete(DbHelper.FAV_TABLE_NAME,DbHelper.MOVIE_ID + " = ? ",new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    //Update method from Content Resolver call this method
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (urimatcher.match(uri)){
            case DATA_ALL_ROWS:
                count = db.update(DbHelper.FAV_TABLE_NAME,values,selection,selectionArgs);
                break;
            case DATA_SINGLE_ROW:
                count = db.update(DbHelper.FAV_TABLE_NAME,values,DbHelper.MOVIE_ID + " = ?",new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
