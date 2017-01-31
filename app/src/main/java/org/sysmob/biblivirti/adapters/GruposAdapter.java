package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.enums.ETipoGrupo;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class GruposAdapter extends RecyclerView.Adapter<GruposAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;
    private List<Grupo> grupos;
    private Usuario usuario;

    public GruposAdapter(Context context, List<Grupo> grupos, Usuario usuario) {
        this.context = context;
        this.grupos = grupos;
        this.usuario = usuario;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_grupos, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.grupos.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Grupo grupo = this.grupos.get(position);
        holder.imageGrupoPrivado.setVisibility(grupo.getGrctipo().equals(ETipoGrupo.FECHADO) ? View.VISIBLE : View.GONE);
        holder.imageAdmin.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), grupo.getAdmin().getUsnid() == this.usuario.getUsnid() ? R.mipmap.ic_king_100px_blue : R.mipmap.ic_king_100px_gray));
        if (grupo.getGrcfoto() != null && !grupo.getGrcfoto().equals("null")) {
            Picasso.with(this.context).load(grupo.getGrcfoto()).into(holder.imageGRCFOTO);
        } else {
            holder.imageGRCFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_group_80px));
        }
        holder.textGRCNOME.setText(grupo.getGrcnome().toString());
        holder.textAICDESC.setText(grupo.getAreaInteresse().getAicdesc().toString());
        holder.textUSCLOGN.setText(grupo.getAdmin().getUsnid() == this.usuario.getUsnid() ? "Você" : grupo.getAdmin().getUsclogn().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageGrupoPrivado;
        ImageView imageAdmin;
        ImageView imageGRCFOTO;
        TextView textGRCNOME;
        TextView textAICDESC;
        TextView textUSCLOGN;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            imageGrupoPrivado = (ImageView) view.findViewById(R.id.imageGrupoPrivado);
            imageAdmin = (ImageView) view.findViewById(R.id.imageAdmin);
            imageGRCFOTO = (ImageView) view.findViewById(R.id.imageGRCFOTO);
            textGRCNOME = (TextView) view.findViewById(R.id.textGRCNOME);
            textAICDESC = (TextView) view.findViewById(R.id.textAICDESC);
            textUSCLOGN = (TextView) view.findViewById(R.id.textUSCLOGN);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onCLick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (onLongClickListener != null) {
                return onLongClickListener.onLongClick(view, getAdapterPosition());
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        public void onCLick(View view, int position);
    }

    public interface OnLongClickListener {
        public boolean onLongClick(View view, int position);
    }

}
