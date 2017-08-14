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
import org.sysmob.biblivirti.activities.PesquisarUsuariosActivity;
import org.sysmob.biblivirti.adapters.PesquisaUsuariosFragmentAdapter;
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
public class PesquisarUsuariosFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerUsuarios;
    private List<Usuario> usuarios;
    private Grupo grupo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar_usuarios, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.layoutEmpty.setVisibility(this.usuarios == null ? View.VISIBLE : View.GONE);

        this.recyclerUsuarios = (RecyclerView) view.findViewById(R.id.recyclerUsuarios);
        this.recyclerUsuarios.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerUsuarios.setHasFixedSize(true);
        this.recyclerUsuarios.setAdapter(new PesquisaUsuariosFragmentAdapter(getContext(), this.usuarios, this.grupo));
        ((PesquisaUsuariosFragmentAdapter) this.recyclerUsuarios.getAdapter()).setOnItemClickListener(new PesquisaUsuariosFragmentAdapter.OnItemClickListener() {
            @Override
            public void onCLick(View view, int position) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(PesquisarUsuariosFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    // Verifica se o usuario clicado EH membro do grupo em questao (ACAO REMOVER DO GRUPO)
                    if (Collections.binarySearch(grupo.getUsuarios(), usuarios.get(position), new UsuarioComparatorByUsnid()) >= 0) {
                        try {
                            if (new GroupBO(getActivity()).validateUnsubscribe()) {
                                Bundle fields = new Bundle();
                                fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                                fields.putInt(Usuario.FIELD_USNID, grupo.getAdmin().getUsnid());
                                fields.putInt(Usuario.FIELD_USNID2, usuarios.get(position).getUsnid());
                                actionSairGrupo(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    } else { // O usuario clicado NAO EH membro do grupo em questao (ACAO ADICIONAR AO GRUPO)
                        try {
                            if (new GroupBO(getActivity()).validateSubscribe()) {
                                Bundle fields = new Bundle();
                                fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                                fields.putInt(Usuario.FIELD_USNID, usuarios.get(position).getUsnid());
                                actionParticiparGrupo(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
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
        this.recyclerUsuarios.setEnabled(status);
    }

    /*****************************************************
     * PUBLIC METHODS
     *****************************************************/
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuaios) {
        this.usuarios = usuaios;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
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
            new NetworkConnection(PesquisarUsuariosFragment.this.getContext()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(PesquisarUsuariosFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        PesquisarUsuariosFragment.this.getContext(),
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
                                Toast.makeText(PesquisarUsuariosFragment.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                MembrosFragment.hasDataChanged = true;
                                ((PesquisarUsuariosActivity) getActivity()).finish();
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
            new NetworkConnection(PesquisarUsuariosFragment.this.getContext()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(PesquisarUsuariosFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        PesquisarUsuariosFragment.this.getContext(),
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
                                Toast.makeText(PesquisarUsuariosFragment.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                MembrosFragment.hasDataChanged = true;
                                ((PesquisarUsuariosActivity) getActivity()).finish();
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
