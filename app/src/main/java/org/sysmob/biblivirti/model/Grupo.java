package org.sysmob.biblivirti.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.sysmob.biblivirti.enums.EStatusGrupo;
import org.sysmob.biblivirti.enums.ETipoGrupo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class Grupo implements Serializable {

    public static final String KEY_GRUPO = "grupo";
    public static final String KEY_GRUPOS = "grupos";

    public static final String FIELD_GRNID = "grnid";
    public static final String FIELD_GRCNOME = "grcnome";
    public static final String FIELD_GRNIDAI = "grnidai";
    public static final String FIELD_GRCFOTO = "grcfoto";
    public static final String FIELD_GRCTIPO = "grctipo";
    public static final String FIELD_GRCSTAT = "grcstat";
    public static final String FIELD_GRDCADT = "grdcadt";
    public static final String FIELD_GRDALDT = "grdaldt";
    public static final String FIELD_GRAREAOFINTEREST = "areaofinterest";
    public static final String FIELD_GRADMIN = "admin";
    public static final String FIELD_GRUSERS = "users";

    private int grnid;
    private AreaInteresse areaInteresse;
    private String grcnome;
    private String grcfoto;
    private ETipoGrupo grctipo;
    private EStatusGrupo grcstat;
    private Usuario admin;
    private Date grdcadt;
    private Date grdaldt;
    private List<Usuario> usuarios;

    public Grupo() {
    }

    public Grupo(int grnid, AreaInteresse areaInteresse, String grcnome, String grcfoto, ETipoGrupo grctipo, EStatusGrupo grcstat, Usuario admin, Date grdcadt, Date grdaldt, List<Usuario> usuarios) {
        this.grnid = grnid;
        this.areaInteresse = areaInteresse;
        this.grcnome = grcnome;
        this.grcfoto = grcfoto;
        this.grctipo = grctipo;
        this.grcstat = grcstat;
        this.admin = admin;
        this.grdcadt = grdcadt;
        this.grdaldt = grdaldt;
        this.usuarios = usuarios;
    }

    public int getGrnid() {
        return grnid;
    }

    public void setGrnid(int grnid) {
        this.grnid = grnid;
    }

    public AreaInteresse getAreaInteresse() {
        return areaInteresse;
    }

    public void setAreaInteresse(AreaInteresse areaInteresse) {
        this.areaInteresse = areaInteresse;
    }

    public String getGrcnome() {
        return grcnome;
    }

    public void setGrcnome(String grcnome) {
        this.grcnome = grcnome;
    }

    public String getGrcfoto() {
        return grcfoto;
    }

    public void setGrcfoto(String grcfoto) {
        this.grcfoto = grcfoto;
    }

    public ETipoGrupo getGrctipo() {
        return grctipo;
    }

    public void setGrctipo(ETipoGrupo grctipo) {
        this.grctipo = grctipo;
    }

    public EStatusGrupo getGrcstat() {
        return grcstat;
    }

    public void setGrcstat(EStatusGrupo grcstat) {
        this.grcstat = grcstat;
    }

    public Usuario getAdmin() {
        return admin;
    }

    public void setAdmin(Usuario admin) {
        this.admin = admin;
    }

    public Date getGrdcadt() {
        return grdcadt;
    }

    public void setGrdcadt(Date grdcadt) {
        this.grdcadt = grdcadt;
    }

    public Date getGrdaldt() {
        return grdaldt;
    }

    public void setGrdaldt(Date grdaldt) {
        this.grdaldt = grdaldt;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
