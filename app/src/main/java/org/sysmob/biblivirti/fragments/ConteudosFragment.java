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
import org.sysmob.biblivirti.adapters.ConteudosAdapter;
import org.sysmob.biblivirti.adapters.OpcoesConteudosAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.dialogs.NovoEditarConteudoDialog;
import org.sysmob.biblivirti.dialogs.OpcoesConteudosDialog;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.List;

public class ConteudosFragment extends Fragment {

    public static boolean hasDataChanged;

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerConteudos;
    private FloatingActionButton buttonNovoConteudo;
    private List<Conteudo> conteudos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita as opcoes de menu no fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_conteudos, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.fragment_conteudos_menu_pesquisar).getActionView();
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
                intent.addCategory(BiblivirtiConstants.INTENT_CATEGORY_PESQUISAR_CONTEUDO);
                Bundle fields = new Bundle();
                fields.putString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, query);
                intent.putExtras(fields);
                /*startActivity(intent);*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), query);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conteudos, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.recyclerConteudos = (RecyclerView) view.findViewById(R.id.recyclerConteudos);
        this.buttonNovoConteudo = (FloatingActionButton) view.findViewById(R.id.buttonNovoConteudo);
        this.buttonNovoConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    actionNovoConteudo(null);
                }
            }
        });

        Bundle fields = new Bundle();
        fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
        actionCarregarConteudos(fields);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConteudosFragment.hasDataChanged) {
            ConteudosFragment.hasDataChanged = false;
            this.conteudos = null;
            Bundle fields = new Bundle();
            fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
            actionCarregarConteudos(fields);
        }
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadFields() {
        if (conteudos == null) {
            layoutEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Nenhum contúdo encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            this.recyclerConteudos = (RecyclerView) this.getView().findViewById(R.id.recyclerConteudos);
            this.recyclerConteudos.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerConteudos.setHasFixedSize(true);
            this.recyclerConteudos.setAdapter(new ConteudosAdapter(getContext(), conteudos));
            ((ConteudosAdapter) this.recyclerConteudos.getAdapter()).setOnItemClickListener(new ConteudosAdapter.OnItemClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    // Verifica se o usuario logado EH ADMIN do grupo
                    if (BiblivirtiApplication.getInstance().getLoggedUser().getUsnid() == ((GrupoActivity) getActivity()).getGrupo().getAdmin().getUsnid()) {
                        Toast.makeText(ConteudosFragment.this.getActivity(), String.format("recyclerConteudos.onCLick: %d", position), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((ConteudosAdapter) this.recyclerConteudos.getAdapter()).setOnLongClickListener(new ConteudosAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position) {
                    // Verifica se o usuario logado EH ADMIN do grupo
                    if (BiblivirtiApplication.getInstance().getLoggedUser().getUsnid() == ((GrupoActivity) getActivity()).getGrupo().getAdmin().getUsnid()) {
                        final OpcoesConteudosDialog dialog = new OpcoesConteudosDialog();
                        dialog.setOnOptionsClickListener(new OpcoesConteudosAdapter.OnItemClickListener() {
                            @Override
                            public void onCLick(View view, int position) {
                                Toast.makeText(getContext(), String.format("OpcoesConteudosDialog.onLongClick: %d", position), Toast.LENGTH_SHORT).show();
                                switch (position) {
                                    case 0: // EDITAR
                                        // FALTA IMPLEMENTAR ESSE PROCESSAMENTO
                                        break;
                                    case 1: // EXCLUIR
                                        // FALTA IMPLEMENTAR ESSE PROCESSAMENTO
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show(getFragmentManager(), OpcoesConteudosDialog.class.getClass().getSimpleName());
                        return false;
                    } else {
                        return false;
                    }
                }
            });
        }

    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionNovoConteudo(Bundle fields) {
        NovoEditarConteudoDialog dialog = new NovoEditarConteudoDialog();
        dialog.setGrupo(((GrupoActivity) getActivity()).getGrupo());
        dialog.setDialogMode(BiblivirtiConstants.DIALOG_MODE_INSERTING);
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (ConteudosFragment.hasDataChanged) {
                    ConteudosFragment.hasDataChanged = false;
                    conteudos = null;
                    Bundle fields = new Bundle();
                    fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
                    actionCarregarConteudos(fields);
                }
            }
        });
        dialog.show(getFragmentManager(), NovoEditarConteudoDialog.class.getSimpleName());
    }

    public void actionCarregarConteudos(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_CONTENT_LIST,
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
                                layoutEmpty.setVisibility(View.GONE);
                                conteudos = BiblivirtiParser.parseToConteudos(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
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
