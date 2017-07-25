package org.sysmob.biblivirti.model;

import org.sysmob.biblivirti.enums.EStatusMaterial;
import org.sysmob.biblivirti.enums.ETipoMaterial;

import java.util.Date;
import java.util.List;

/**
 * Created by micro12 on 21/06/2017.
 */

public class Video extends Material {

    public Video() {
        super();
        this.setMactipo(ETipoMaterial.VIDEO);
    }

    public Video(int manid, String macdesc, String macurl, ETipoMaterial mactipo, EStatusMaterial macstat, Date madcadt, Date madaldt, int manqtdce, int manqtdha, List<Conteudo> conteudos, List<Comentario> comentarios) {
        super(manid, macdesc, macurl, ETipoMaterial.VIDEO, macstat, madcadt, madaldt, manqtdce, manqtdha, conteudos, comentarios);
    }

}
