package com.example.punit.popularmovies.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Helper class used to detect current Network status of device.
 */

public class ConnectionDetector {

    private Context mContext;

    public ConnectionDetector(Context context){
        mContext = context;
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork !=null && activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }
}
