package org.sysmob.biblivirti.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.sysmob.biblivirti.enums.ERecuperarSenhaStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by djalmocruzjr on 15/01/2017.
 */

public class RecuperarSenha implements Serializable {

    public static final String KEY_RECUPERAR_SENHA = "RecuperarSenha";

    public static final String FIELD_RSNID = "rsnid";
    public static final String FIELD_RSNIDUS = "rsnidus";
    public static final String FIELD_RSCTOKN = "rsctokn";
    public static final String FIELD_RSCSTAT = "rscstat";
    public static final String FIELD_RSDCADT = "rsdcadt";
    public static final String FIELD_RSDALDT = "rsdaldt";

    private int rsnid;
    private int rsnidus;
    private String rsctokn;
    private ERecuperarSenhaStatus rscstat;
    private Date rsdcadt;
    private Date rsdaldt;

    public RecuperarSenha() {
    }

    public RecuperarSenha(int rsnid, int rsnidus, String rsctokn, ERecuperarSenhaStatus rscstat, Date rsdcadt, Date rsdaldt) {
        this.rsnid = rsnid;
        this.rsnidus = rsnidus;
        this.rsctokn = rsctokn;
        this.rscstat = rscstat;
        this.rsdcadt = rsdcadt;
        this.rsdaldt = rsdaldt;
    }

    public int getRsnid() {
        return rsnid;
    }

    public void setRsnid(int rsnid) {
        this.rsnid = rsnid;
    }

    public int getRsnidus() {
        return rsnidus;
    }

    public void setRsnidus(int rsnidus) {
        this.rsnidus = rsnidus;
    }

    public String getRsctokn() {
        return rsctokn;
    }

    public void setRsctokn(String rsctokn) {
        this.rsctokn = rsctokn;
    }

    public ERecuperarSenhaStatus getRscstat() {
        return rscstat;
    }

    public void setRscstat(ERecuperarSenhaStatus rscstat) {
        this.rscstat = rscstat;
    }

    public Date getRsdcadt() {
        return rsdcadt;
    }

    public void setRsdcadt(Date rsdcadt) {
        this.rsdcadt = rsdcadt;
    }

    public Date getRsdaldt() {
        return rsdaldt;
    }

    public void setRsdaldt(Date rsdaldt) {
        this.rsdaldt = rsdaldt;
    }

}
