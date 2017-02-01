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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.activities.HomeActivity;
import org.sysmob.biblivirti.activities.NovoGrupoActivity;
import org.sysmob.biblivirti.adapters.GruposAdapter;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;

import java.util.List;

public class GruposEstudoFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerGrupos;
    private FloatingActionButton buttonNovoGrupo;
    private List<Grupo> grupos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grupos_estudo, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_grupos_estudo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.fragment_grupos_estudo_menu_pesquisar:
                Toast.makeText(getActivity(), "Pesquisar grupos!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadData() {
        if (grupos == null) {
            // Nenhum grupo encontrado
            Toast.makeText(getActivity(), "Nenhum grupo encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            this.recyclerGrupos = (RecyclerView) this.getView().findViewById(R.id.recyclerGrupos);
            this.recyclerGrupos.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerGrupos.setHasFixedSize(true);
            this.recyclerGrupos.setAdapter(new GruposAdapter(getActivity(), grupos, ((HomeActivity) getActivity()).getUsuario()));
            ((GruposAdapter) this.recyclerGrupos.getAdapter()).setOnItemClickListener(new GruposAdapter.OnItemClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    Toast.makeText(getActivity(), String.format("OnItemClickListener(): Posição %d", position), Toast.LENGTH_SHORT).show();
                }
            });
            ((GruposAdapter) this.recyclerGrupos.getAdapter()).setOnLongClickListener(new GruposAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position) {
                    Toast.makeText(getActivity(), String.format("OnLongClickListener(): Posição %d", position), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public void notifyNewGroupAdded() {
        Toast.makeText(getActivity(), "Novo grupo adicionado!", Toast.LENGTH_SHORT).show();
        // Implementar reconstrucao do fragment
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    private void actionNovoGrupo(Bundle fields) {
        Intent intent = new Intent(getActivity(), NovoGrupoActivity.class);
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
                                grupos = BiblivirtiParser.parseToGrupos(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
                                loadData();
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
