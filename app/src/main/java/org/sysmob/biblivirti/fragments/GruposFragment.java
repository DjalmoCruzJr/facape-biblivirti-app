package org.sysmob.biblivirti.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.activities.GrupoActivity;
import org.sysmob.biblivirti.activities.HomeActivity;
import org.sysmob.biblivirti.activities.InfoGrupoActivity;
import org.sysmob.biblivirti.activities.NovoEditarGrupoActivity;
import org.sysmob.biblivirti.adapters.GruposAdapter;
import org.sysmob.biblivirti.adapters.OpcoesGruposAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.GroupBO;
import org.sysmob.biblivirti.dialogs.OpcoesGruposDialog;
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

public class GruposFragment extends Fragment {

    public static boolean hasDataChanged;

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerGrupos;
    private FloatingActionButton buttonNovoGrupo;
    private List<Grupo> grupos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita as opcoes de menu no fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grupos, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.buttonNovoGrupo = (FloatingActionButton) view.findViewById(R.id.buttonNovoGrupo);
        this.buttonNovoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle fields = new Bundle();
                fields.putSerializable(Usuario.KEY_USUARIO, ((HomeActivity) getActivity()).getUsuario());
                actionNovoGrupo(fields);
            }
        });

        Bundle fields = new Bundle();
        fields.putInt(Usuario.FIELD_USNID, ((HomeActivity) getActivity()).getUsuario().getUsnid());
        actionCarregarGrupos(fields);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_grupos_estudo, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.fragment_grupos_estudo_menu_pesquisar).getActionView();
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
                intent.addCategory(BiblivirtiConstants.INTENT_CATEGORY_PESQUISAR_GRUPO);
                Bundle fields = new Bundle();
                fields.putString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, query);
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
        if (GruposFragment.hasDataChanged) {
            GruposFragment.hasDataChanged = false;
            this.grupos = null;
            Bundle fields = new Bundle();
            fields.putInt(Usuario.FIELD_USNID, ((HomeActivity) getActivity()).getUsuario().getUsnid());
            actionCarregarGrupos(fields);
        }
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadFields() {
        if (grupos == null) {
            // Nenhum grupo encontrado
            layoutEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Nenhum grupo encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            this.recyclerGrupos = (RecyclerView) this.getView().findViewById(R.id.recyclerGrupos);
            this.recyclerGrupos.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerGrupos.setHasFixedSize(true);
            this.recyclerGrupos.setAdapter(new GruposAdapter(getActivity(), grupos, ((HomeActivity) getActivity()).getUsuario()));
            ((GruposAdapter) this.recyclerGrupos.getAdapter()).setOnItemClickListener(new GruposAdapter.OnItemClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    Bundle extras = new Bundle();
                    extras.putSerializable(Grupo.KEY_GRUPO, grupos.get(position));
                    Intent intent = new Intent(GruposFragment.this.getContext(), GrupoActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            ((GruposAdapter) this.recyclerGrupos.getAdapter()).setOnLongClickListener(new GruposAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position) {
                    final OpcoesGruposDialog dialog = new OpcoesGruposDialog();
                    dialog.setGrupo(grupos.get(position));
                    dialog.setOnOptionsClickListener(new OpcoesGruposAdapter.OnItemClickListener() {
                        @Override
                        public void onCLick(View view, int position) {
                            Intent intent;
                            Bundle extras;
                            if (!BiblivirtiUtils.isNetworkConnected()) {
                                String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            } else {
                                switch (position) {
                                    case OpcoesGruposDialog.OPTION_INFO_GRUPO:
                                        extras = new Bundle();
                                        extras.putInt(Grupo.FIELD_GRNID, dialog.getGrupo().getGrnid());
                                        intent = new Intent(GruposFragment.this.getActivity(), InfoGrupoActivity.class);
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        break;
                                    case OpcoesGruposDialog.OPTION_EDITAR:
                                        extras = new Bundle();
                                        extras.putInt(Grupo.FIELD_GRNID, dialog.getGrupo().getGrnid());
                                        extras.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_EDITING);
                                        extras.putString(BiblivirtiConstants.ACTIVITY_TITLE, getString(R.string.activity_editar_grupo_label));
                                        intent = new Intent(GruposFragment.this.getActivity(), NovoEditarGrupoActivity.class);
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        break;
                                    case OpcoesGruposDialog.OPTION_EXCLUIR:
                                        try {
                                            if (new GroupBO(getActivity()).validateDelete()) {
                                                BiblivirtiDialogs.showConfirmationDialog(
                                                        getContext(),
                                                        "Confirmaçao",
                                                        "Deseja realmete excluir este grupo ?",
                                                        "Sim",
                                                        "Não",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface confirmationDialog, int which) {
                                                                Bundle extras = new Bundle();
                                                                extras.putInt(Grupo.FIELD_GRNID, dialog.getGrupo().getGrnid());
                                                                extras.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                                                                actionExcluirGrupo(extras);
                                                                confirmationDialog.dismiss();
                                                            }
                                                        }
                                                );

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
                    dialog.show(getFragmentManager(), OpcoesGruposDialog.class.getSimpleName());
                    return true;
                }
            });
        }
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    private void actionExcluirGrupo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_DELETE,
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
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        getActivity(),
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                        ),
                                        "Ok"
                                );
                            } else {
                                Toast.makeText(getActivity(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                GruposFragment.hasDataChanged = true;
                                onResume();
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

    private void actionNovoGrupo(Bundle fields) {
        Intent intent = new Intent(getActivity(), NovoEditarGrupoActivity.class);
        intent.putExtras(fields);
        startActivity(intent);
    }

    private void actionCarregarGrupos(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_LIST,
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
                                Toast.makeText(GruposFragment.this.getContext(), message, Toast.LENGTH_SHORT).show();
                                /*BiblivirtiDialogs.showMessageDialog(
                                        getActivity(),
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                        ),
                                        "Ok"
                                );*/
                            } else {
                                layoutEmpty.setVisibility(View.GONE);
                                grupos = BiblivirtiParser.parseToGrupos(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
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

}
