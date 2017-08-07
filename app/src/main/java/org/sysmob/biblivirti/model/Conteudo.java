package org.sysmob.biblivirti.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by micro99 on 23/02/2017.
 */
public class Conteudo implements Serializable {

    public static final String KEY_CONTENT = "content";
    public static final String KEY_CONTENTS = "contents";

    public static final String FIELD_CONID = "conid";
    public static final String FIELD_COCDESC = "cocdesc";
    public static final String FIELD_CODCADT = "codcadt";
    public static final String FIELD_CODALDT = "codaldt";
    public static final String FIELD_CONIDGR = "conidgr";
    public static final String FIELD_GROUP = "group";

    private boolean isSelected;

    private int conid;
    private String cocdesc;
    private Date codcadt;
    private Date codaldt;
    private Grupo grupo;

    public Conteudo() {
    }

    public Conteudo(int conid, String cocdesc, Date codcadt, Date codaldt, Grupo grupo) {
        this.conid = conid;
        this.cocdesc = cocdesc;
        this.codcadt = codcadt;
        this.codaldt = codaldt;
        this.grupo = grupo;

        this.isSelected = false;
    }

    public int getConid() {
        return conid;
    }

    public void setConid(int conid) {
        this.conid = conid;
    }

    public String getCocdesc() {
        return cocdesc;
    }

    public void setCocdesc(String cocdesc) {
        this.cocdesc = cocdesc;
    }

    public Date getCodcadt() {
        return codcadt;
    }

    public void setCodcadt(Date codcadt) {
        this.codcadt = codcadt;
    }

    public Date getCodaldt() {
        return codaldt;
    }

    public void setCodaldt(Date codaldt) {
        this.codaldt = codaldt;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
