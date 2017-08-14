package org.sysmob.biblivirti.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.activities.GrupoActivity;
import org.sysmob.biblivirti.activities.PerfilActivity;
import org.sysmob.biblivirti.adapters.MembrosAdapter;
import org.sysmob.biblivirti.adapters.OpcoesMembrosAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.GroupBO;
import org.sysmob.biblivirti.dialogs.OpcoesMembrosDialog;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.List;

public class MembrosFragment extends Fragment {

    public static boolean hasDataChanged;

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMembros;
    private Button buttonSairGrupo;
    private List<Usuario> membros;
    private Usuario usuarioLogado;
    private Grupo grupo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita as opcoes de menu no fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.grupo = ((GrupoActivity) getActivity()).getGrupo();
        this.usuarioLogado = BiblivirtiApplication.getInstance().getLoggedUser();

        View view = inflater.inflate(R.layout.fragment_membros, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.buttonSairGrupo = (Button) view.findViewById(R.id.buttonSairGrupo);
        this.buttonSairGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (new GroupBO(getActivity()).validateUnsubscribe()) {
                        Bundle fields = new Bundle();
                        fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                        fields.putInt(Usuario.FIELD_USNID, grupo.getAdmin().getUsnid());
                        fields.putInt(Usuario.FIELD_USNID2, usuarioLogado.getUsnid());
                        actionSairGrupo(fields);
                    }
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle fields = new Bundle();
        fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity)

                getActivity()).

                getGrupo().

                getGrnid());

        actionCarregarMembros(fields);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_membros, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.fragment_membros_menu_pesquisar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.activity_pesquisar_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.length() <= 0) {
                    return false;
                }
                Log.i(String.format("%s: ", getClass().getSimpleName().toString()), query);
                Intent intent = new Intent(BiblivirtiConstants.INTENT_ACTION_PESQUISAR);
                intent.addCategory(BiblivirtiConstants.INTENT_CATEGORY_PESQUISAR_USUARIO);
                Bundle fields = new Bundle();
                fields.putString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, query);
                fields.putSerializable(Grupo.KEY_GRUPO, ((GrupoActivity) getActivity()).getGrupo());
                intent.putExtras(fields);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Toast.makeText(getActivity(), String.format("onQueryTextChange: %s", query), Toast.LENGTH_SHORT).show();
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), query);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MembrosFragment.hasDataChanged) {
            MembrosFragment.hasDataChanged = false;
            this.membros = null;
            Bundle fields = new Bundle();
            fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
            actionCarregarMembros(fields);
        }
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/

    private void enableWidgets(boolean status) {
        this.progressBar.setEnabled(status);
        this.layoutEmpty.setEnabled(status);
        this.recyclerMembros.setEnabled(status);
        this.buttonSairGrupo.setEnabled(status);
    }

    private void loadFields() {
        // Verifica se nenhum membro foi encontrado
        if (membros == null) {
            layoutEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Nenhum membro encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            this.recyclerMembros = (RecyclerView) this.getView().findViewById(R.id.recyclerMembros);
            this.recyclerMembros.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerMembros.setHasFixedSize(true);
            this.recyclerMembros.setAdapter(new MembrosAdapter(getActivity(), membros, ((GrupoActivity) getActivity()).getGrupo()));
            ((MembrosAdapter) this.recyclerMembros.getAdapter()).setOnLongClickListener(new MembrosAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position) {
                    final OpcoesMembrosDialog dialog = new OpcoesMembrosDialog();
                    dialog.setMembro(membros.get(position));
                    dialog.setGrupo(((GrupoActivity) getActivity()).getGrupo());
                    dialog.setOnOptionsClickListener(new OpcoesMembrosAdapter.OnItemClickListener() {
                        @Override
                        public void onCLick(View view, int position) {
                            Intent intent;
                            Bundle extras;
                            if (!BiblivirtiUtils.isNetworkConnected()) {
                                String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            } else {
                                switch (position) {
                                    case OpcoesMembrosDialog.OPTION_VER_PERFIL:
                                        extras = new Bundle();
                                        extras.putInt(Usuario.FIELD_USNID, dialog.getMembro().getUsnid());
                                        intent = new Intent(getActivity(), PerfilActivity.class);
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        break;
                                    case OpcoesMembrosDialog.OPTION_ENVIAR_EMAIL:
                                        Toast.makeText(getActivity(), "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case OpcoesMembrosDialog.OPTION_REMOVER_DO_GRUPO:
                                        try {
                                            if (new GroupBO(getActivity()).validateUnsubscribe()) {
                                                Bundle fields = new Bundle();
                                                fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                                                fields.putInt(Usuario.FIELD_USNID, grupo.getAdmin().getUsnid());
                                                fields.putInt(Usuario.FIELD_USNID2, dialog.getMembro().getUsnid());
                                                actionSairGrupo(fields);
                                            }
                                        } catch (ValidationException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
                                        break;
                                }

                            }
                        }
                    });
                    dialog.show(getFragmentManager(), OpcoesMembrosDialog.class.getSimpleName());
                    return true;
                }
            });
        }

    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    private void actionCarregarMembros(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_ACCOUNT_GROUP_MEMBERS_LIST,
                    params
            );
            new NetworkConnection(getActivity()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        layoutEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                layoutEmpty.setVisibility(View.VISIBLE);
                                String message = String.format(
                                        "Código: %d\n%s",
                                        response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                        response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                );
                                Toast.makeText(MembrosFragment.this.getContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                layoutEmpty.setVisibility(View.GONE);
                                membros = BiblivirtiParser.parseToUsuarios(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
                                loadFields();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onAfterRequest(String response) {
                }
            });
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    public void actionSairGrupo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            params.put(Usuario.FIELD_USNID2, fields.getInt(Usuario.FIELD_USNID2));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_UNSUBSCRIBE,
                    params
            );
            new NetworkConnection(MembrosFragment.this.getContext()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(MembrosFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                String message = String.format(
                                        "Código: %d\n%s\n%s",
                                        response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                        response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                        BiblivirtiUtils.createStringErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS))
                                );
                                BiblivirtiDialogs.showMessageDialog(
                                        MembrosFragment.this.getContext(),
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                                BiblivirtiUtils.createStringErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS))
                                        ),
                                        "Ok"
                                );
                                Toast.makeText(MembrosFragment.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MembrosFragment.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                GruposFragment.hasDataChanged = true;
                                ((GrupoActivity) getActivity()).finish();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.GONE);
                    enableWidgets(true);
                }

                @Override
                public void onAfterRequest(String response) {
                }
            });
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

}
