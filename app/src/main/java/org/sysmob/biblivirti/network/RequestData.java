package org.sysmob.biblivirti.network;

import org.json.JSONObject;

import java.io.Serializable;

public class RequestData implements Serializable {

    private int method;
    private String url;
    private JSONObject params;

    public RequestData() {
    }

    public RequestData(int method, String url, JSONObject params) {
        this.method = method;
        this.url = url;
        this.params = params;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
