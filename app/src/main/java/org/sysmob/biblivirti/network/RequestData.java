package org.sysmob.biblivirti.network;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class RequestData implements Serializable {

    private String tag;
    private int method;
    private String url;
    private JSONObject params;
    private Map<String, String> strParams;

    public RequestData() {
    }

    public RequestData(String tag, int method, String url, JSONObject params) {
        this.tag = tag;
        this.method = method;
        this.url = url;
        this.params = params;
    }

    public RequestData(String tag, int method, String url, Map<String, String> strParams) {
        this.tag = tag;
        this.method = method;
        this.url = url;
        this.strParams = strParams;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public Map<String, String> getStrParams() {
        return strParams;
    }

    public void setStrParams(Map<String, String> strParams) {
        this.strParams = strParams;
    }
}
