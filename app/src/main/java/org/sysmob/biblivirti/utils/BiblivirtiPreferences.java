package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by djalmocruzjr on 08/01/2017.
 */

public abstract class BiblivirtiPreferences {

    private static final String PREFERENCE_FILE_NAME = "org.sysmob.biblivirti.BIBLIVIRTI_PREFERENCES";

    public static final String PREFERENCE_PROPERTY_EMAIL = "org.sysmob.biblivirti.PREFERENCE_PROPERTY_EMAIL";
    public static final String PREFERENCE_PROPERTY_SENHA = "org.sysmob.biblivirti.PREFERENCE_PROPERTY_SENHA";

    public static String getProperty(Context context, String propertyName) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String value = preferences.getString(propertyName, null);
        return value;
    }

    public static boolean saveProperty(Context context, String propertyName, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(propertyName, value);
        return editor.commit();
    }

    public static boolean deleteProperty(Context context, String propertyName) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(propertyName);
        return editor.commit();
    }

}
