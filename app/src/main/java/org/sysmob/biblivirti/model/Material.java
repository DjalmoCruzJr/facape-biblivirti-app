package org.sysmob.biblivirti.model;

import org.sysmob.biblivirti.enums.EStatusMaterial;
import org.sysmob.biblivirti.enums.ETipoMaterial;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by micro99 on 23/02/2017.
 */
public abstract class Material implements Serializable {

    public static final String KEY_MATERIAL = "material";
    public static final String KEY_MATERIAIS = "materiais";

    public static final String FIELD_MANID = "manid";
    public static final String FIELD_MACDESC = "macdesc";
    public static final String FIELD_MACTIPO = "mactipo";
    public static final String FIELD_MACURL = "macurl";
    public static final String FIELD_MACNIVL = "macnivl";
    public static final String FIELD_MACSTAT = "macstat";
    public static final String FIELD_MADCADT = "madcadt";
    public static final String FIELD_MADALDT = "madaldt";
    public static final String FIELD_MANQTDCE = "manqtdce";
    public static final String FIELD_MANQTDHA = "manqtdha";
    public static final String FIELD_CONTENTS = "contents";

    private int manid;
    private String macdesc;
    private String macurl;
    private ETipoMaterial mactipo;
    private EStatusMaterial macstat;
    private Date madcadt;
    private Date madaldt;
    private int manqtdce;
    private int manqtdha;
    private List<Conteudo> conteudos;
    private List<Comentario> comentarios;

    public Material() {
    }

    public Material(int manid, String macdesc, String macurl, ETipoMaterial mactipo, EStatusMaterial macstat, Date madcadt, Date madaldt, int manqtdce, int manqtdha, List<Conteudo> conteudos, List<Comentario> comentarios) {
        this.manid = manid;
        this.macdesc = macdesc;
        this.macurl = macurl;
        this.mactipo = mactipo;
        this.macstat = macstat;
        this.madcadt = madcadt;
        this.madaldt = madaldt;
        this.manqtdce = manqtdce;
        this.manqtdha = manqtdha;
        this.conteudos = conteudos;
        this.comentarios = comentarios;
    }

    public int getManid() {
        return manid;
    }

    public void setManid(int manid) {
        this.manid = manid;
    }

    public String getMacdesc() {
        return macdesc;
    }

    public void setMacdesc(String macdesc) {
        this.macdesc = macdesc;
    }

    public String getMacurl() {
        return macurl;
    }

    public void setMacurl(String macurl) {
        this.macurl = macurl;
    }

    public ETipoMaterial getMactipo() {
        return mactipo;
    }

    public void setMactipo(ETipoMaterial mactipo) {
        this.mactipo = mactipo;
    }

    public EStatusMaterial getMacstat() {
        return macstat;
    }

    public void setMacstat(EStatusMaterial macstat) {
        this.macstat = macstat;
    }

    public Date getMadcadt() {
        return madcadt;
    }

    public void setMadcadt(Date madcadt) {
        this.madcadt = madcadt;
    }

    public Date getMadaldt() {
        return madaldt;
    }

    public void setMadaldt(Date madaldt) {
        this.madaldt = madaldt;
    }

    public int getManqtdce() {
        return manqtdce;
    }

    public void setManqtdce(int manqtdce) {
        this.manqtdce = manqtdce;
    }

    public int getManqtdha() {
        return manqtdha;
    }

    public void setManqtdha(int manqtdha) {
        this.manqtdha = manqtdha;
    }

    public List<Conteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
}
