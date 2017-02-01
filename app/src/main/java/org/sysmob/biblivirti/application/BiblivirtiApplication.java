package org.sysmob.biblivirti.application;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.sysmob.biblivirti.model.Usuario;

public class BiblivirtiApplication extends Application {

    public static final String TAG = BiblivirtiApplication.class.getSimpleName();

    private static BiblivirtiApplication instance;
    private static Context context;

    private RequestQueue requestQueue;
    private Usuario loggedUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized BiblivirtiApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) == true ? TAG : tag);
        VolleyLog.d("Adcionando requisição: %s", request.getUrl());
        this.requestQueue.add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        VolleyLog.d("Adcionando requisição: %s", request.getUrl());
        this.requestQueue.add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.requestQueue != null) {
            this.requestQueue.cancelAll(tag);
        }
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }
}
