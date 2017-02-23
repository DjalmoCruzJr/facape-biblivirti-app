package org.sysmob.biblivirti.model;

import org.sysmob.biblivirti.enums.ENivelSimulado;
import org.sysmob.biblivirti.enums.EStatusMaterial;
import org.sysmob.biblivirti.enums.ETipoMaterial;

import java.util.Date;
import java.util.List;

/**
 * Created by micro99 on 23/02/2017.
 */

public class Simulado extends Material {

    private ENivelSimulado macnivl;
    private List<Questao> questaos;

    public Simulado() {
        super();
    }

    public Simulado(int manid, String macdesc, String macurl, EStatusMaterial macstat, ENivelSimulado macnivl, Date madcadt, Date madaldt, int manqtdce, int manqtdha, List<Conteudo> conteudos, List<Comentario> comentarios, List<Questao> questaos) {
        super(manid, macdesc, macurl, ETipoMaterial.SIMULADO, macstat, madcadt, madaldt, manqtdce, manqtdha, conteudos, comentarios);
        this.macnivl = macnivl;
        this.questaos = questaos;
    }

    public ENivelSimulado getMacnivl() {
        return macnivl;
    }

    public void setMacnivl(ENivelSimulado macnivl) {
        this.macnivl = macnivl;
    }

    public List<Questao> getQuestaos() {
        return questaos;
    }

    public void setQuestaos(List<Questao> questaos) {
        this.questaos = questaos;
    }
}
