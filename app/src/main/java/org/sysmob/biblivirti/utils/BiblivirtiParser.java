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

    public static List<Grupo> parseToGrupos(JSONArray json) throws JSONException {
        List<Grupo> grupos = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            grupos.add(
                    new Grupo(
                            json.getJSONObject(i).getInt(Grupo.FIELD_GRNID),
                            parseToAreaInteresse(json.getJSONObject(i).getJSONObject(Grupo.FIELD_GRAREAOFINTEREST)),
                            json.getJSONObject(i).getString(Grupo.FIELD_GRCNOME),
                            json.getJSONObject(i).getString(Grupo.FIELD_GRCFOTO),
                            ETipoGrupo.ABERTO.getValue() == json.getJSONObject(i).getString(Grupo.FIELD_GRCTIPO).charAt(0) ? ETipoGrupo.ABERTO : ETipoGrupo.FECHADO,
                            EStatusGrupo.ATIVO.getValue() == json.getJSONObject(i).getString(Grupo.FIELD_GRCSTAT).charAt(0) ? EStatusGrupo.ATIVO : EStatusGrupo.INATIVO,
                            parseToUsuario(json.getJSONObject(i).getJSONObject(Grupo.FIELD_GRADMIN)),
                            Timestamp.valueOf(json.getJSONObject(i).getString(Grupo.FIELD_GRDCADT)),
                            Timestamp.valueOf(json.getJSONObject(i).getString(Grupo.FIELD_GRDALDT))
                    )
            );
        }
        return grupos.size() > 0 ? grupos : null;
    }

    public static AreaInteresse parseToAreaInteresse(JSONObject json) throws JSONException {
        AreaInteresse areaInteresse = new AreaInteresse();
        areaInteresse.setAinid(json.getInt(AreaInteresse.FIELD_AINID));
        areaInteresse.setAicdesc(json.getString(AreaInteresse.FIELD_AICDESC));
        areaInteresse.setAidcadt(Timestamp.valueOf(json.getString(AreaInteresse.FIELD_AIDCADT)));
        areaInteresse.setAidaldt(Timestamp.valueOf(json.getString(AreaInteresse.FIELD_AIDALDT)));
        return areaInteresse;

    }

    public static List<AreaInteresse> parseToAreasinteresse(JSONArray json) throws JSONException {
        List<AreaInteresse> areasInteresse = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            areasInteresse.add(
                    new AreaInteresse(
                            json.getJSONObject(i).getInt(AreaInteresse.FIELD_AINID),
                            json.getJSONObject(i).getString(AreaInteresse.FIELD_AICDESC),
                            Timestamp.valueOf(json.getJSONObject(i).getString(AreaInteresse.FIELD_AIDCADT)),
                            Timestamp.valueOf(json.getJSONObject(i).getString(AreaInteresse.FIELD_AIDALDT))
                    )
            );
        }
        return areasInteresse.size() > 0 ? areasInteresse : null;
    }
}
