package com.example.punit.popularmovies.Helpers;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ApiRequest {
    private static final Gson gson = new GsonBuilder().create();

    public static GsonGetRequest<UpcomingMovies> getMovieObject(
            @NonNull final String url,
            @NonNull final Response.Listener<UpcomingMovies> listener,
            @NonNull final Response.ErrorListener errorListener
    ){
        return new GsonGetRequest<>(
                url,
                new TypeToken<UpcomingMovies>(){}.getType(),
                gson,
                listener,
                errorListener);
    }
}
