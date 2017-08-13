package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.comparators.UsuarioComparatorByUsnid;
import org.sysmob.biblivirti.enums.ETipoGrupo;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.Collections;
import java.util.List;

/**
 * Created by micro99 on 15/02/2017.
 */
public class PerfilGruposAdapter extends RecyclerView.Adapter<PerfilGruposAdapter.ViewHolder> {

    private Context context;
    private List<Grupo> grupos;
    private Usuario usuario;
    private OnItemClickListener onItemClickListener;
    private Usuario usuarioLogado;

    public PerfilGruposAdapter(Context context, List<Grupo> grupos, Usuario usuario) {
        this.context = context;
        this.grupos = grupos;
        this.usuario = usuario;
        this.usuarioLogado = BiblivirtiApplication.getInstance().getLoggedUser();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_perfil_grupos, parent, false);
        PerfilGruposAdapter.ViewHolder viewHolder = new PerfilGruposAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Grupo grupo = this.grupos.get(position);
        holder.imageGRCFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_group_80px));
        if (grupo.getGrcfoto() != null && !grupo.getGrcfoto().equals("null")) {
            Picasso.with(this.context).load(grupo.getGrcfoto()).into(holder.imageGRCFOTO);
        }
        holder.imageGrupoPrivado.setVisibility(grupo.getGrctipo() == ETipoGrupo.FECHADO ? View.VISIBLE : View.INVISIBLE);
        if (grupo.getAdmin().getUsnid() == usuario.getUsnid()) {
            holder.imageAdmin.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_king_100px_blue));
        } else {
            holder.imageAdmin.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_king_100px_gray));
        }
        holder.textGRCNOME.setText((grupo.getGrcnome().toString()));
        holder.textAICDESC.setText(grupo.getAreaInteresse().getAicdesc().toString());
        holder.textADMIN.setText(grupo.getAdmin().getUsclogn().toString().equalsIgnoreCase(usuarioLogado.getUsclogn().toString()) ? "Você" : grupo.getAdmin().getUsclogn().toString());
        // Verifica se o usuario logado EH membro do grupo
        if (Collections.binarySearch(grupo.getUsuarios(), usuarioLogado, new UsuarioComparatorByUsnid()) >= 0) {
            holder.buttonSairParticiparGrupo.setBackgroundColor(context.getResources().getColor(R.color.colorRedDark));
            holder.buttonSairParticiparGrupo.setText(context.getResources().getString(R.string.adapter_list_perfil_grupos_button_sair_participar_sair));
            holder.buttonSairParticiparGrupo.setVisibility(usuarioLogado.getUsnid() == grupo.getAdmin().getUsnid() ? View.GONE : View.VISIBLE);
        } else { //O usuario logado NAO EH membro do grupo
            holder.buttonSairParticiparGrupo.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.buttonSairParticiparGrupo.setText(context.getResources().getString(R.string.adapter_list_perfil_grupos_button_sair_participar_participar));
            holder.buttonSairParticiparGrupo.setVisibility(grupo.getGrctipo() == ETipoGrupo.ABERTO ? View.VISIBLE : View.GONE);
        }
        holder.buttonSairParticiparGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onCLick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.grupos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageGRCFOTO;
        ImageView imageGrupoPrivado;
        ImageView imageAdmin;
        TextView textGRCNOME;
        TextView textAICDESC;
        TextView textADMIN;
        Button buttonSairParticiparGrupo;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageGRCFOTO = (ImageView) view.findViewById(R.id.imageGRCFOTO);
            imageGrupoPrivado = (ImageView) view.findViewById(R.id.imageGrupoPrivado);
            imageAdmin = (ImageView) view.findViewById(R.id.imageAdmin);
            textGRCNOME = (TextView) view.findViewById(R.id.textGRCNOME);
            textAICDESC = (TextView) view.findViewById(R.id.textAICDESC);
            textADMIN = (TextView) view.findViewById(R.id.textADMIN);
            buttonSairParticiparGrupo = (Button) view.findViewById(R.id.buttonSairParticiparGrupo);
            buttonSairParticiparGrupo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, String.format("buttonSairParticiparGrupo.onClick(): %d", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onCLick(view, getAdapterPosition());
            }
        }

    }

    public interface OnItemClickListener {
        public void onCLick(View view, int position);
    }

}
