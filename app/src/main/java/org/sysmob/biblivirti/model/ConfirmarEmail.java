package org.sysmob.biblivirti.model;

import org.sysmob.biblivirti.enums.EConfirmarEmailStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by djalmocruzjr on 15/01/2017.
 */

public class ConfirmarEmail implements Serializable {

    public static final String KEY_CONFIRMAR_EMAIL = "confirmarEmail";

    public static final String FIELD_CANID = "canid";
    public static final String FIELD_CANIDUS = "canidus";
    public static final String FIELD_CACTOKN = "cactokn";
    public static final String FIELD_CACSTAT = "cacstat";
    public static final String FIELD_CADCADT = "cadcadt";
    public static final String FIELD_CADALDT = "cadaldt";

    private int canid;
    private int canidus;
    private String cactokn;
    private EConfirmarEmailStatus cacstat;
    private Date cadcadt;
    private Date cadaldt;

    public ConfirmarEmail() {
    }

    public ConfirmarEmail(int canid, int canidus, String cactokn, EConfirmarEmailStatus cacstat, Date cadcadt, Date cadaldt) {
        this.canid = canid;
        this.canidus = canidus;
        this.cactokn = cactokn;
        this.cacstat = cacstat;
        this.cadcadt = cadcadt;
        this.cadaldt = cadaldt;
    }

    public int getCanid() {
        return canid;
    }

    public void setCanid(int canid) {
        this.canid = canid;
    }

    public int getCanidus() {
        return canidus;
    }

    public void setCanidus(int canidus) {
        this.canidus = canidus;
    }

    public String getCactokn() {
        return cactokn;
    }

    public void setCactokn(String cactokn) {
        this.cactokn = cactokn;
    }

    public EConfirmarEmailStatus getCacstat() {
        return cacstat;
    }

    public void setCacstat(EConfirmarEmailStatus cacstat) {
        this.cacstat = cacstat;
    }

    public Date getCadcadt() {
        return cadcadt;
    }

    public void setCadcadt(Date cadcadt) {
        this.cadcadt = cadcadt;
    }

    public Date getCadaldt() {
        return cadaldt;
    }

    public void setCadaldt(Date cadaldt) {
        this.cadaldt = cadaldt;
    }
}
