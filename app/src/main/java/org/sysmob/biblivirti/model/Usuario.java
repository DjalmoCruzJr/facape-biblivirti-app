package org.sysmob.biblivirti.model;

import org.sysmob.biblivirti.enums.EUsuarioStatus;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {

    public static final String KEY_USUARIO = "usuario";

    public static final String FIELD_USNID = "usnid";
    public static final String FIELD_USCFBID = "uscfbid";
    public static final String FIELD_USCNOME = "uscnome";
    public static final String FIELD_USCMAIL = "uscmail";
    public static final String FIELD_USCLOGN = "usclogn";
    public static final String FIELD_USCFOTO = "uscfoto";
    public static final String FIELD_USCSENH = "uscsenh";
    public static final String FIELD_USCSTAT = "uscstat";
    public static final String FIELD_USDCADT = "usdcadt";
    public static final String FIELD_USDALDT = "usdaldt";

    private int usnid;
    private String uscfbid;
    private String uscnome;
    private String uscmail;
    private String usclogn;
    private String uscfoto;
    private String uscsenh;
    private EUsuarioStatus uscstat;
    private Date usdcadt;
    private Date usdaldt;

    public Usuario() {
    }

    public Usuario(int usnid, String uscfbid, String uscnome, String uscmail, String usclogn, String uscfoto, String uscsenh, EUsuarioStatus uscstat, Date usdcadt, Date usdaldt) {
        this.usnid = usnid;
        this.uscfbid = uscfbid;
        this.uscnome = uscnome;
        this.uscmail = uscmail;
        this.usclogn = usclogn;
        this.uscfoto = uscfoto;
        this.uscsenh = uscsenh;
        this.uscstat = uscstat;
        this.usdcadt = usdcadt;
        this.usdaldt = usdaldt;
    }

    public int getUsnid() {
        return usnid;
    }

    public void setUsnid(int usnid) {
        this.usnid = usnid;
    }

    public String getUscfbid() {
        return uscfbid;
    }

    public void setUscfbid(String uscfbid) {
        this.uscfbid = uscfbid;
    }

    public String getUscnome() {
        return uscnome;
    }

    public void setUscnome(String uscnome) {
        this.uscnome = uscnome;
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

    public String getUscfoto() {
        return uscfoto;
    }

    public void setUscfoto(String uscfoto) {
        this.uscfoto = uscfoto;
    }

    public String getUscsenh() {
        return uscsenh;
    }

    public void setUscsenh(String uscsenh) {
        this.uscsenh = uscsenh;
    }

    public EUsuarioStatus getUscstat() {
        return uscstat;
    }

    public void setUscstat(EUsuarioStatus uscstat) {
        this.uscstat = uscstat;
    }

    public Date getUsdcadt() {
        return usdcadt;
    }

    public void setUsdcadt(Date usdcadt) {
        this.usdcadt = usdcadt;
    }

    public Date getUsdaldt() {
        return usdaldt;
    }

    public void setUsdaldt(Date usdaldt) {
        this.usdaldt = usdaldt;
    }
}
