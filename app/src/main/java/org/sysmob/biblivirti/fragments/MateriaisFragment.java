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
import org.sysmob.biblivirti.activities.NovoEditarMaterialActivity;
import org.sysmob.biblivirti.adapters.MateriaisAdapter;
import org.sysmob.biblivirti.adapters.OpcoesMateriaisAdapter;
import org.sysmob.biblivirti.adapters.TiposMateriaisDialogAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.comparators.MaterialComparatorByMacaldt;
import org.sysmob.biblivirti.dialogs.AnexarLinkarMaterialDialog;
import org.sysmob.biblivirti.dialogs.OpcoesMateriaisDialog;
import org.sysmob.biblivirti.dialogs.TiposMateriaisDialog;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.model.Simulado;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.Collections;
import java.util.List;

public class MateriaisFragment extends Fragment {

    public static boolean hasDataChanged;

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMateriais;
    private FloatingActionButton buttonNovoMaterial;
    private List<Material> materiais;
    private Material material;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita as opcoes de menu no fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MateriaisFragment.hasDataChanged) {
            MateriaisFragment.hasDataChanged = false;
            Bundle fields = new Bundle();
            fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
            actionCarregarMateriais(fields);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materiais, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.recyclerMateriais = (RecyclerView) view.findViewById(R.id.recyclerMateriais);
        this.buttonNovoMaterial = (FloatingActionButton) view.findViewById(R.id.buttonNovoMaterial);
        this.buttonNovoMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    actionNovoMaterial(null);
                }
            }
        });

        Bundle fields = new Bundle();
        fields.putInt(Grupo.FIELD_GRNID, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
        actionCarregarMateriais(fields);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_materiais, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.fragment_materiais_menu_pesquisar).getActionView();
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
                intent.addCategory(BiblivirtiConstants.INTENT_CATEGORY_PESQUISAR_MATERIAL);
                Bundle fields = new Bundle();
                fields.putInt(Material.FIELD_MANIDGR, ((GrupoActivity) getActivity()).getGrupo().getGrnid());
                fields.putString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, query);
                intent.putExtras(fields);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), query);
                return false;
            }
        });
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadFields() {
        if (materiais == null) {
            layoutEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Nenhum material encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            Collections.sort(this.materiais, new MaterialComparatorByMacaldt()); // Ordena decrescente por data de alteracao

            //this.recyclerMateriais = (RecyclerView) this.getView().findViewById(R.id.recyclerMateriais);
            this.recyclerMateriais.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerMateriais.setHasFixedSize(true);
            this.recyclerMateriais.setAdapter(new MateriaisAdapter(getContext(), this.materiais));
            ((MateriaisAdapter) this.recyclerMateriais.getAdapter()).setOnItemClickListener(new MateriaisAdapter.OnItemClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    Toast.makeText(MateriaisFragment.this.getActivity(), String.format("recyclerMateriais.onCLick: %d", position), Toast.LENGTH_SHORT).show();
                }
            });
            ((MateriaisAdapter) this.recyclerMateriais.getAdapter()).setOnLongClickListener(new MateriaisAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position) {
                    final OpcoesMateriaisDialog dialog = new OpcoesMateriaisDialog();
                    dialog.setMaterial(materiais.get(position));
                    dialog.setOnOptionsClickListener(new OpcoesMateriaisAdapter.OnItemClickListener() {
                        @Override
                        public void onCLick(View view, int position) {
                            Intent intent;
                            Bundle extras;
                            if (!BiblivirtiUtils.isNetworkConnected()) {
                                String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            } else {
                                switch (position) {
                                    case OpcoesMateriaisDialog.OPTION_COMPARTILHAR:
                                        Toast.makeText(getContext(), "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case OpcoesMateriaisDialog.OPTION_ENVIAR_EMAIL:
                                        Toast.makeText(getContext(), "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case OpcoesMateriaisDialog.OPTION_EDITAR:
                                        extras = new Bundle();
                                        extras.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_EDITING);
                                        extras.putString(BiblivirtiConstants.ACTIVITY_TITLE, getResources().getString(R.string.activity_novo_editar_material_label_edit));
                                        extras.putSerializable(Grupo.KEY_GRUPO, ((GrupoActivity) getActivity()).getGrupo());
                                        extras.putSerializable(Material.KEY_MATERIAL, dialog.getMaterial());
                                        intent = new Intent(MateriaisFragment.this.getActivity(), NovoEditarMaterialActivity.class);
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        break;
                                    case OpcoesMateriaisDialog.OPTION_EXCLUIR:
                                        BiblivirtiDialogs.showConfirmationDialog(
                                                getContext(),
                                                "Confirmação",
                                                "Deseja realmente excluir este material ?",
                                                "Sim",
                                                "Não",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface confirmationDialog, int i) {
                                                        Bundle fields = new Bundle();
                                                        fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                                                        fields.putInt(Material.FIELD_MANID, dialog.getMaterial().getManid());
                                                        actionExcluirMaterial(fields);
                                                        confirmationDialog.dismiss();
                                                    }
                                                }
                                        );
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), OpcoesMateriaisDialog.class.getSimpleName());
                    return true;
                }
            });
        }

    }


    /********************************************************
     * ACTION METHODS
     *******************************************************/
    private void actionCarregarMateriais(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_MATERIAL_LIST,
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
                                Toast.makeText(MateriaisFragment.this.getContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                layoutEmpty.setVisibility(View.GONE);
                                materiais = BiblivirtiParser.parseToMateriais(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
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

    private void actionNovoMaterial(Bundle fields) {
        final TiposMateriaisDialog tiposMateriaisDialog = new TiposMateriaisDialog();
        tiposMateriaisDialog.setOnOptionsClickListener(new TiposMateriaisDialogAdapter.OnItemClickListener() {
            @Override
            public void onCLick(View view, int position) {
                if (ETipoMaterial.values()[position] == ETipoMaterial.SIMULADO) {
                    Bundle extras = new Bundle();
                    extras.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_INSERTING);
                    extras.putString(BiblivirtiConstants.ACTIVITY_TITLE, getResources().getString(R.string.activity_novo_editar_material_label_insert));
                    extras.putSerializable(Grupo.KEY_GRUPO, ((GrupoActivity) getActivity()).getGrupo());
                    extras.putSerializable(Material.KEY_MATERIAL, new Simulado());
                    Intent intent = new Intent(MateriaisFragment.this.getContext(), NovoEditarMaterialActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    AnexarLinkarMaterialDialog anexarLinkarMaterialDialog = new AnexarLinkarMaterialDialog();
                    anexarLinkarMaterialDialog.setGrupo(((GrupoActivity) getActivity()).getGrupo());
                    anexarLinkarMaterialDialog.setTipoMaterial(ETipoMaterial.values()[position]);
                    anexarLinkarMaterialDialog.show(getFragmentManager(), AnexarLinkarMaterialDialog.class.getSimpleName());
                }

                tiposMateriaisDialog.dismiss();
            }
        });
        tiposMateriaisDialog.show(this.getFragmentManager(), TiposMateriaisDialog.class.getSimpleName());
    }

    private void actionExcluirMaterial(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            params.put(Material.FIELD_MANID, fields.getInt(Material.FIELD_MANID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_MATERIAL_DELETE,
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
                                        getContext(),
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
                                layoutEmpty.setVisibility(View.GONE);
                                Toast.makeText(getContext(), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                MateriaisFragment.this.hasDataChanged = true;
                                MateriaisFragment.this.onResume();
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
