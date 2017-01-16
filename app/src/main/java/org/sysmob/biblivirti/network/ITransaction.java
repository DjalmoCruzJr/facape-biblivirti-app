package org.sysmob.biblivirti.network;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public interface ITransaction {

    public void onBeforeRequest();

    public void onAfterRequest(JSONObject response);

    public void onAfterRequest(String response);
}
