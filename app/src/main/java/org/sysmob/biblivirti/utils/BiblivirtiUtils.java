package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

    public static String getMimeType(String type) {
        String mimeType = "";
        switch (type) {
            case "image/jpeg":
            case "image/jpg":
                mimeType = "jpeg";
                break;
            case "image/png":
                mimeType = "png";
                break;
            default:
                mimeType = "jpeg";
        }
        return mimeType;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String encondImage(Bitmap bitmap, String mimeType) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT) + "." + getMimeType(mimeType);
    }
}
