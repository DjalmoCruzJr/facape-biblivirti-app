package org.sysmob.biblivirti.model;

import java.io.Serializable;

/**
 * Created by djalmocruzjr on 08/01/2017.
 */

public class Usuario implements Serializable {

    private String usnid;
    private String uscnome;
    private String uscfoto;
    private String uscmail;
    private String usclogn;
    private String uscsenh;

    public Usuario() {
    }

    public String getUsnid() {
        return usnid;
    }

    public void setUsnid(String usnid) {
        this.usnid = usnid;
    }

    public String getUscnome() {
        return uscnome;
    }

    public void setUscnome(String uscnome) {
        this.uscnome = uscnome;
    }

    public String getUscfoto() {
        return uscfoto;
    }

    public void setUscfoto(String uscfoto) {
        this.uscfoto = uscfoto;
    }

    public String getUscmail() {
        return uscmail;
    }

    public void setUscmail(String uscmail) {
        this.uscmail = uscmail;
    }

    public String getUsclogn() {
        return usclogn;
    }

    public void setUsclogn(String usclogn) {
        this.usclogn = usclogn;
    }

    public String getUscsenh() {
        return uscsenh;
    }

    public void setUscsenh(String uscsenh) {
        this.uscsenh = uscsenh;
    }
}
