package org.sysmob.biblivirti.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */
public class AreaInteresse implements Serializable {

    public static final String KEY_AREA_INTERESSE = "areainteresse";

    public static final String FIELD_AINID = "ainid";
    public static final String FIELD_AICDESC = "aicdesc";
    public static final String FIELD_AIDCADT = "aidcadt";
    public static final String FIELD_AIDALDT = "aidaldt";

    private int ainid;
    private String aicdesc;
    private Date aidcadt;
    private Date aidaldt;

    public AreaInteresse() {
    }

    public AreaInteresse(int ainid, String aicdesc, Date aidcadt, Date aidaldt) {
        this.ainid = ainid;
        this.aicdesc = aicdesc;
        this.aidcadt = aidcadt;
        this.aidaldt = aidaldt;
    }

    public int getAinid() {
        return ainid;
    }

    public void setAinid(int ainid) {
        this.ainid = ainid;
    }

    public String getAicdesc() {
        return aicdesc;
    }

    public void setAicdesc(String aicdesc) {
        this.aicdesc = aicdesc;
    }

    public Date getAidcadt() {
        return aidcadt;
    }

    public void setAidcadt(Date aidcadt) {
        this.aidcadt = aidcadt;
    }

    public Date getAidaldt() {
        return aidaldt;
    }

    public void setAidaldt(Date aidaldt) {
        this.aidaldt = aidaldt;
    }
}
