package org.sysmob.biblivirti.network;

import android.app.Activity;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.sysmob.biblivirti.application.BiblivirtiApplication;

/**
 * Created by djalmocruzjr on 09/01/2017.
 */

public class NetworkConnection {

    private ITransaction transaction;
    private RequestQueue requestQueue;

    public NetworkConnection() {
    }

    public NetworkConnection(Context context, ITransaction transaction) {
        this.transaction = transaction;
        this.requestQueue = ((BiblivirtiApplication) ((Activity) context).getApplication()).getRequestQueue();
    }

    public ITransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(ITransaction transaction) {
        this.transaction = transaction;
    }

    /******************************************
     * ACTION METHOD
     *****************************************/
    public void execute(RequestData requestData) {
        this.transaction.onBeforeRequest();
        this.executeRequest(requestData);
    }

    /******************************************
     * PRIVATE METHODS
     *****************************************/
    private void executeRequest(final RequestData requestData) {
        JsonObjectRequest request = new JsonObjectRequest(
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
                        transaction.onAfterRequest(null);
                    }
                }
        );

        this.requestQueue.add(request);
    }

}
