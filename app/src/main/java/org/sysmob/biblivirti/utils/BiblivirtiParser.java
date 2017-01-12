package org.sysmob.biblivirti.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.enums.EUsuarioStatus;
import org.sysmob.biblivirti.model.Usuario;

import java.sql.Timestamp;

public abstract class BiblivirtiParser {

    public static Usuario parseToUsuario(JSONObject json) throws JSONException {
        Usuario usuario = new Usuario();
        usuario.setUsnid(json.getInt(Usuario.FIELD_USNID));
        usuario.setUscfbid(json.getString(Usuario.FIELD_USCFBID));
        usuario.setUscnome(json.getString(Usuario.FIELD_USCNOME));
        usuario.setUscmail(json.getString(Usuario.FIELD_USCMAIL));
        usuario.setUsclogn(json.getString(Usuario.FIELD_USCLOGN));
        usuario.setUscfoto(json.getString(Usuario.FIELD_USCFOTO));
        usuario.setUscsenh(json.opt(Usuario.FIELD_USCSENH) == null ? null : json.getString(Usuario.FIELD_USCSENH));
        usuario.setUscstat(EUsuarioStatus.ATIVO.getValue() == json.getString(Usuario.FIELD_USCSTAT).charAt(0) ? EUsuarioStatus.ATIVO : EUsuarioStatus.INATIVO);
        usuario.setUsdcadt(Timestamp.valueOf(json.getString(Usuario.FIELD_USDCADT)));
        usuario.setUsdaldt(Timestamp.valueOf(json.getString(Usuario.FIELD_USDALDT)));
        return usuario;
    }
}
