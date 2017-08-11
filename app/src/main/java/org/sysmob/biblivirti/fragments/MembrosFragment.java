package org.sysmob.biblivirti.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import org.sysmob.biblivirti.activities.GrupoActivity;
import org.sysmob.biblivirti.activities.HomeActivity;
import org.sysmob.biblivirti.adapters.GruposAdapter;
import org.sysmob.biblivirti.dialogs.OpcoesGruposDialog;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.List;

public class MembrosFragment extends Fragment {

    public static boolean hasDataChanged;

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMembros;
    private FloatingActionButton buttonNovoMembro;
    private List<Usuario> membros;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita as opcoes de menu no fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membros, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.buttonNovoMembro = (FloatingActionButton) view.findViewById(R.id.buttonNovoMembro);

        Bundle fields = new Bundle();
        fields.putInt(Usuario.FIELD_USNID, ((HomeActivity) getActivity()).getUsuario().getUsnid());
        actionCarregarMembros(fields);

        return view;
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadFields() {
        if (membros == null) {
            // Nenhum membro encontrado
            layoutEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Nenhum membro encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            this.recyclerMembros = (RecyclerView) this.getView().findViewById(R.id.recyclerGrupos);
            this.recyclerMembros.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerMembros.setHasFixedSize(true);
            this.recyclerMembros.setAdapter(new MembrosAdapter(getActivity(), membros, ((HomeActivity) getActivity()).getUsuario()));
            ((GruposAdapter) this.recyclerMembros.getAdapter()).setOnItemClickListener(new GruposAdapter.OnItemClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    Bundle extras = new Bundle();
                    extras.putSerializable(Grupo.KEY_GRUPO, membros.get(position));
                    Intent intent = new Intent(MembrosFragment.this.getContext(), GrupoActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            ((GruposAdapter) this.recyclerMembros.getAdapter()).setOnLongClickListener(new GruposAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position) {
                    final OpcoesMembrosDialog dialog = new OpcoesMembrosDialog();
                    dialog.setMembro(membros.get(position));
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
                                    case OpcoesMembrosDialog.OPTION_INFO_GRUPO:
                                        break;
                                    case OpcoesMembrosDialog.OPTION_EDITAR:
                                        break;
                                    case OpcoesMembrosDialog.OPTION_EXCLUIR:
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
     * PUBLIC METHODS
     *******************************************************/

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
}
