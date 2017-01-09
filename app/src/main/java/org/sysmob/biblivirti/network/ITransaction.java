package org.sysmob.biblivirti.network;

import org.json.JSONObject;

public interface ITransaction {

    public void onBeforeRequest();

    public void onAfterRequest(JSONObject response);
}
