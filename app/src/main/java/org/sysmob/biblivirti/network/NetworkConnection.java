package org.sysmob.biblivirti.network;

import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;

/**
 * Created by djalmocruzjr on 09/01/2017.
 */

public class NetworkConnection {

    private RequestQueue requestQueue;

    public NetworkConnection() {
    }

    public NetworkConnection(Context context) {
        this.requestQueue = ((BiblivirtiApplication) ((Activity) context).getApplication()).getRequestQueue();
    }

    /******************************************
     * ACTION METHOD
     *****************************************/
    public void execute(RequestData requestData, ITransaction transaction) {
        transaction.onBeforeRequest();
        this.executeRequest(requestData, transaction);
    }

    /******************************************
     * PRIVATE METHODS
     *****************************************/

    private void executeRequest(final RequestData requestData, final ITransaction transaction) {
        Request request = null;
        if (requestData.getMethod() == Request.Method.GET) {
            request = new StringRequest(
                    requestData.getMethod(),
                    requestData.getUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            transaction.onAfterRequest(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            transaction.onAfterRequest((String) null);
                        }
                    }
            );
        } else {
            request = new JsonObjectRequest(
                    requestData.getMethod(),
                    requestData.getUrl(),
                    requestData.getParams(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            transaction.onAfterRequest(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            transaction.onAfterRequest((JSONObject) null);
                            error.printStackTrace();
                        }
                    }
            );
        }
        request.setTag(transaction.getClass().getSimpleName());
        request.setRetryPolicy(new DefaultRetryPolicy(
                BiblivirtiConstants.REQUEST_TIMEOUT,
                BiblivirtiConstants.DEFAULT_MAX_RETRIES,
                BiblivirtiConstants.DEFAULT_BACKOFF_MULT
        ));
        this.requestQueue.add(request);
    }

}
