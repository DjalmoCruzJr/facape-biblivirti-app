package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import org.sysmob.biblivirti.application.BiblivirtiApplication;

import java.io.ByteArrayOutputStream;

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

    public static String encondImage(Bitmap bitmap, String mimeType) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT) + "." + mimeType;
    }
}
