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

import com.squareup.picasso.Picasso;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by micro99 on 15/02/2017.
 */
public class InfoMembrosAdapter extends RecyclerView.Adapter<InfoMembrosAdapter.ViewHolder> {

    private Context context;
    private List<Usuario> usuarios;
    private Usuario admin;
    private OnItemClickListener onItemClickListener;
    private Usuario loggedUser;

    public InfoMembrosAdapter(Context context, List<Usuario> usuarios, Usuario admin) {
        this.context = context;
        this.usuarios = usuarios;
        this.admin = admin;
        this.loggedUser = BiblivirtiApplication.getInstance().getLoggedUser();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_info_grupo_membros, parent, false);
        InfoMembrosAdapter.ViewHolder viewHolder = new InfoMembrosAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Usuario usuario = this.usuarios.get(position);
        holder.imageAdmin.setVisibility(this.admin.getUsnid() == usuario.getUsnid() ? View.VISIBLE : View.GONE);
        holder.imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_user_80px));
        if (usuario.getUscfoto() != null && !usuario.getUscfoto().equals("null")) {
            Picasso.with(this.context).load(usuario.getUscfoto()).into(holder.imageUSCFOTO);
        }
        holder.editUSCNOME.setText((usuario.getUscnome() != null && !usuario.getUscnome().equals("null")) ? usuario.getUscnome().toString() : usuario.getUsclogn().toString());
        holder.editUSCMAIL.setText(usuario.getUscmail().toString());
        holder.editGRDCADT.setText(new SimpleDateFormat("dd/MM/yyy HH:mm").format(usuario.getUsdcadt()));
        holder.buttonRemoverDoGrupo.setVisibility(this.admin.getUsnid() == this.loggedUser.getUsnid() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return this.usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageUSCFOTO;
        ImageView imageAdmin;
        TextView editUSCNOME;
        TextView editUSCMAIL;
        TextView editGRDCADT;
        Button buttonRemoverDoGrupo;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageUSCFOTO = (ImageView) view.findViewById(R.id.imageUSCFOTO);
            imageAdmin = (ImageView) view.findViewById(R.id.imageAdmin);
            editUSCNOME = (TextView) view.findViewById(R.id.textUSCNOME);
            editUSCMAIL = (TextView) view.findViewById(R.id.textUSCMAIL);
            editGRDCADT = (TextView) view.findViewById(R.id.textGRDCADT);
            buttonRemoverDoGrupo = (Button) view.findViewById(R.id.buttonRemoverDoGrupo);
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
