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
        if (this.getStrParams() != null) {
            this.url += "?";
            for (int i = 0; i < this.getStrParams().size(); i++) {
                this.url += (String) this.getStrParams().keySet().toArray()[i];
                this.url += "=";
                this.url += (String) this.getStrParams().values().toArray()[i];
                if (i + 1 < this.getStrParams().size()) {
                    this.url += "&";
                }
            }
        }
        return this.url;
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
