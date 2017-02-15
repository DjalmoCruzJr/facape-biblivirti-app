package org.sysmob.biblivirti.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.enums.EStatusGrupo;
import org.sysmob.biblivirti.enums.ETipoGrupo;
import org.sysmob.biblivirti.enums.EUsuarioStatus;
import org.sysmob.biblivirti.model.AreaInteresse;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
        return usuario != null ? usuario : null;
    }

    public static List<Usuario> parseToUsuarios(JSONArray json) throws JSONException {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = null;
        for (int i = 0; i < json.length(); i++) {
            usuario = parseToUsuario(json.getJSONObject(i));
            usuarios.add(usuario);
        }
        return usuarios.size() > 0 ? usuarios : null;
    }

    public static AreaInteresse parseToAreaInteresse(JSONObject json) throws JSONException {
        AreaInteresse areaInteresse = new AreaInteresse();
        areaInteresse.setAinid(json.getInt(AreaInteresse.FIELD_AINID));
        areaInteresse.setAicdesc(json.getString(AreaInteresse.FIELD_AICDESC));
        areaInteresse.setAidcadt(Timestamp.valueOf(json.getString(AreaInteresse.FIELD_AIDCADT)));
        areaInteresse.setAidaldt(Timestamp.valueOf(json.getString(AreaInteresse.FIELD_AIDALDT)));
        return areaInteresse != null ? areaInteresse : null;

    }

    public static List<AreaInteresse> parseToAreasinteresse(JSONArray json) throws JSONException {
        List<AreaInteresse> areasInteresse = new ArrayList<>();
        AreaInteresse areaInteresse = null;
        for (int i = 0; i < json.length(); i++) {
            areaInteresse = parseToAreaInteresse(json.getJSONObject(i));
            areasInteresse.add(areaInteresse);
        }
        return areasInteresse.size() > 0 ? areasInteresse : null;
    }

    public static Grupo parseToGrupo(JSONObject json) throws JSONException {
        Grupo grupo = new Grupo();
        grupo.setGrnid(json.getInt(Grupo.FIELD_GRNID));
        grupo.setAreaInteresse(parseToAreaInteresse(json.getJSONObject(Grupo.FIELD_GRAREAOFINTEREST)));
        grupo.setGrcnome(json.getString(Grupo.FIELD_GRCNOME));
        grupo.setGrcfoto(json.getString(Grupo.FIELD_GRCFOTO));
        grupo.setGrctipo(ETipoGrupo.ABERTO.getValue() == json.getString(Grupo.FIELD_GRCTIPO).charAt(0) ? ETipoGrupo.ABERTO : ETipoGrupo.FECHADO);
        grupo.setGrcstat(EStatusGrupo.ATIVO.getValue() == json.getString(Grupo.FIELD_GRCSTAT).charAt(0) ? EStatusGrupo.ATIVO : EStatusGrupo.INATIVO);
        grupo.setGrdcadt(Timestamp.valueOf(json.getString(Grupo.FIELD_GRDCADT)));
        grupo.setGrdaldt(Timestamp.valueOf(json.getString(Grupo.FIELD_GRDALDT)));
        grupo.setUsuarios(parseToUsuarios(json.getJSONArray(Grupo.FIELD_GRUSERS)));
        return grupo != null ? grupo : null;
    }

    public static List<Grupo> parseToGrupos(JSONArray json) throws JSONException {
        List<Grupo> grupos = new ArrayList<>();
        Grupo grupo = null;
        for (int i = 0; i < json.length(); i++) {
            grupo = parseToGrupo(json.getJSONObject(i));
            grupos.add(grupo);
        }
        return grupos.size() > 0 ? grupos : null;
    }
}
