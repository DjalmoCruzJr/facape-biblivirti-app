package org.sysmob.biblivirti.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.activities.PesquisarGruposActivity;
import org.sysmob.biblivirti.adapters.PesquisarGruposFragmentAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.GroupBO;
import org.sysmob.biblivirti.comparators.UsuarioComparatorByUsnid;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarGruposFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerGrupos;
    private List<Grupo> grupos;
    private Usuario usuarioLogado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar_grupos, container, false);

        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.layoutEmpty.setVisibility(this.grupos == null ? View.VISIBLE : View.GONE);
        this.recyclerGrupos = (RecyclerView) view.findViewById(R.id.recyclerGrupos);
        this.recyclerGrupos.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerGrupos.setHasFixedSize(true);
        this.recyclerGrupos.setAdapter(new PesquisarGruposFragmentAdapter(getActivity(), this.grupos, BiblivirtiApplication.getInstance().getLoggedUser()));
        ((PesquisarGruposFragmentAdapter) this.recyclerGrupos.getAdapter()).setOnItemClickListener(new PesquisarGruposFragmentAdapter.OnItemClickListener() {
            @Override
            public void onCLick(View view, int position) {
                // Verifica se o usuario clicado ja EH membro do grupo (ACAO SAIR DO GRUPO)
                if (Collections.binarySearch(grupos.get(position).getUsuarios(), usuarioLogado, new UsuarioComparatorByUsnid()) >= 0) {
                    try {
                        if (new GroupBO(getActivity()).validateUnsubscribe()) {
                            Bundle fields = new Bundle();
                            fields.putInt(Grupo.FIELD_GRNID, grupos.get(position).getGrnid());
                            fields.putInt(Usuario.FIELD_USNID, grupos.get(position).getAdmin().getUsnid());
                            fields.putInt(Usuario.FIELD_USNID2, usuarioLogado.getUsnid());
                            actionSairGrupo(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                } else { // Verifica se o usuario clicado NAO EH membro do grupo (ACAO PARTICIPAR DO GRUPO)
                    try {
                        if (new GroupBO(getActivity()).validateSubscribe()) {
                            Bundle fields = new Bundle();
                            fields.putInt(Grupo.FIELD_GRNID, grupos.get(position).getGrnid());
                            fields.putInt(Usuario.FIELD_USNID, usuarioLogado.getUsnid());
                            actionParticiparGrupo(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
        this.progressBar.setEnabled(status);
        this.layoutEmpty.setEnabled(status);
        this.recyclerGrupos.setEnabled(status);
    }

    /*****************************************************
     * PUBLIC METHODS
     *****************************************************/
    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionParticiparGrupo(final Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_SUBSCRIBE,
                    params
            );
            new NetworkConnection(PesquisarGruposFragment.this.getContext()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(PesquisarGruposFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        PesquisarGruposFragment.this.getContext(),
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                                BiblivirtiUtils.createStringErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS))
                                        ),
                                        "Ok"
                                );
                            } else {
                                Toast.makeText(PesquisarGruposFragment.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                ((PesquisarGruposActivity) getActivity()).loadFields();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
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
            new NetworkConnection(PesquisarGruposFragment.this.getContext()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(PesquisarGruposFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        PesquisarGruposFragment.this.getContext(),
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                                BiblivirtiUtils.createStringErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS))
                                        ),
                                        "Ok"
                                );
                            } else {
                                Toast.makeText(PesquisarGruposFragment.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                ((PesquisarGruposActivity) getActivity()).loadFields();
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
