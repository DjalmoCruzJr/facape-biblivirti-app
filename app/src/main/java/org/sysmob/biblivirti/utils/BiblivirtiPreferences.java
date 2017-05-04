package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by djalmocruzjr on 08/01/2017.
 */

public abstract class BiblivirtiPreferences {

    public static String getProperty(Context context, String propertyName) {
        SharedPreferences preferences = context.getSharedPreferences(BiblivirtiConstants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String value = preferences.getString(propertyName, null);
        return value;
    }

    public static boolean saveProperty(Context context, String propertyName, String value) {
        SharedPreferences preferences = context.getSharedPreferences(BiblivirtiConstants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.putString(propertyName, value).commit();
    }

    public static boolean deleteProperty(Context context, String propertyName) {
        SharedPreferences preferences = context.getSharedPreferences(BiblivirtiConstants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.remove(propertyName).commit();
    }

}
