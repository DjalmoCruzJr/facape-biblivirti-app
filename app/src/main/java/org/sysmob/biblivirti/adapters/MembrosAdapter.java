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
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class MembrosAdapter extends RecyclerView.Adapter<MembrosAdapter.ViewHolder> {

    private Context context;
    private OnLongClickListener onLongClickListener;
    private List<Usuario> membros;
    private Grupo grupo;
    private Usuario usuarioLogado;

    public MembrosAdapter(Context context, List<Usuario> membros, Grupo grupo) {
        this.context = context;
        this.membros = membros;
        this.grupo = grupo;
        this.usuarioLogado = BiblivirtiApplication.getInstance().getLoggedUser();
    }

    public OnLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_membros, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.membros.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Usuario membro = this.membros.get(position);
        holder.imageAdmin.setVisibility(this.grupo.getAdmin().getUsnid() == membro.getUsnid() ? View.VISIBLE : View.GONE);
        holder.imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_user_80px));
        if (membro != null && !membro.getUscfoto().equalsIgnoreCase("null")) {
            Picasso.with(this.context).load(membro.getUscfoto().toString()).into(holder.imageUSCFOTO);
        }
        holder.textUSCNOME.setText(membro.getUscnome() != null ? membro.getUscnome().toString() : membro.getUsclogn().toString());
        holder.textUSCLOGN.setText(membro.getUsclogn().toString());
        holder.textUSCMAIL.setText(membro.getUscmail().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ImageView imageAdmin;
        ImageView imageUSCFOTO;
        TextView textUSCNOME;
        TextView textUSCLOGN;
        TextView textUSCMAIL;

        public ViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            imageAdmin = (ImageView) view.findViewById(R.id.imageAdmin);
            imageUSCFOTO = (ImageView) view.findViewById(R.id.imageUSCFOTO);
            textUSCNOME = (TextView) view.findViewById(R.id.textUSCNOME);
            textUSCLOGN = (TextView) view.findViewById(R.id.textUSCLOGN);
            textUSCMAIL = (TextView) view.findViewById(R.id.textUSCMAIL);
        }

        @Override
        public boolean onLongClick(View view) {
            if (onLongClickListener != null) {
                return onLongClickListener.onLongClick(view, getAdapterPosition());
            }
            return false;
        }
    }

    public interface OnLongClickListener {
        public boolean onLongClick(View view, int position);
    }

}
