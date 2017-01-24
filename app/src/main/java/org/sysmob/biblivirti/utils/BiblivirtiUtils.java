package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.sysmob.biblivirti.application.BiblivirtiApplication;

/**
 * Created by djalmocruzjr on 24/01/2017.
 */

public abstract class BiblivirtiUtils {

    public static boolean isNetworkConnected() {
        Context context = BiblivirtiApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
