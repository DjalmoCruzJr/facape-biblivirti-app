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
    private List<Grupo> grupos;
    private Usuario usuario;

    public GruposAdapter(Context context, List<Grupo> grupos, Usuario usuario) {
        this.context = context;
        this.grupos = grupos;
        this.usuario = usuario;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_grupos, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Grupo grupo = this.grupos.get(position);
        holder.imageGrupoPrivado.setVisibility(grupo.getGrctipo().equals(ETipoGrupo.FECHADO) ? View.VISIBLE : View.GONE);
        holder.imageAdmin.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), grupo.getAdmin().getUsnid() == this.usuario.getUsnid() ? R.mipmap.ic_king_100px_blue : R.mipmap.ic_king_100px_gray));
        Picasso.with(this.context).load(grupo.getGrcfoto()).into(holder.imageGRCFOTO);
        holder.textGRCNOME.setText(grupo.getGrcnome().toString());
        holder.textAICDESC.setText(grupo.getAreaInteresse().getAicdesc().toString());
        holder.textUSCLOGN.setText(grupo.getAdmin().getUsnid() == this.usuario.getUsnid() ? "Você" : grupo.getAdmin().getUsclogn().toString());
    }

    @Override
    public int getItemCount() {
        return this.grupos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageGrupoPrivado;
        ImageView imageAdmin;
        ImageView imageGRCFOTO;
        TextView textGRCNOME;
        TextView textAICDESC;
        TextView textUSCLOGN;

        public ViewHolder(View view) {
            super(view);

            imageGrupoPrivado = (ImageView) view.findViewById(R.id.imageGrupoPrivado);
            imageAdmin = (ImageView) view.findViewById(R.id.imageAdmin);
            imageGRCFOTO = (ImageView) view.findViewById(R.id.imageGRCFOTO);
            textGRCNOME = (TextView) view.findViewById(R.id.textGRCNOME);
            textAICDESC = (TextView) view.findViewById(R.id.textAICDESC);
            textUSCLOGN = (TextView) view.findViewById(R.id.textUSCLOGN);
        }

    }
}
