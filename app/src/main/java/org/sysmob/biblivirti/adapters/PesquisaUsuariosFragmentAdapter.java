package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.comparators.UsuarioComparatorByUsnid;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.Collections;
import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class PesquisaUsuariosFragmentAdapter extends RecyclerView.Adapter<PesquisaUsuariosFragmentAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<Usuario> usuarios;
    private Usuario usuarioLogado;
    private Grupo grupo;

    public PesquisaUsuariosFragmentAdapter(Context context, List<Usuario> usuarios, Grupo grupo) {
        this.context = context;
        this.usuarios = usuarios;
        this.usuarioLogado = BiblivirtiApplication.getInstance().getLoggedUser();
        this.grupo = grupo;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_pesquisar_usuarios, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.usuarioLogado != null ? this.usuarios.size() : 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Usuario usuario = this.usuarios.get(position);
        holder.imageAdmin.setVisibility(this.grupo.getAdmin().getUsnid() == usuario.getUsnid() ? View.VISIBLE : View.GONE);
        holder.imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_user_80px));
        if (usuario.getUscfoto() != null && !usuario.getUscfoto().equals("null")) {
            Picasso.with(this.context).load(usuario.getUscfoto()).into(holder.imageUSCFOTO);
        }
        holder.textUSCNOME.setText((usuario.getUscnome() != null && !usuario.getUscnome().equals("null")) ? usuario.getUscnome().toString() : usuario.getUsclogn().toString());
        holder.textUSCNOME.setText(usuario.getUsnid() == usuarioLogado.getUsnid() ? "Você" : holder.textUSCNOME.getText());
        holder.textUSCLOGN.setText(usuario.getUsclogn().toString());
        holder.textUSCMAIL.setText(usuario.getUscmail().toString());
        // Verifica se o usuario EH membro do grupo em questao
        if (Collections.binarySearch(grupo.getUsuarios(), usuario, new UsuarioComparatorByUsnid()) >= 0) {
            holder.buttonAdicionarRemoverUsuario.setBackgroundColor(context.getResources().getColor(R.color.colorRedDark));
            holder.buttonAdicionarRemoverUsuario.setText(context.getResources().getString(R.string.adapter_list_pesquisar_usuarios_button_adicionar_remover_usuario_remover));
            holder.buttonAdicionarRemoverUsuario.setVisibility(usuario.getUsnid() != grupo.getAdmin().getUsnid() && grupo.getAdmin().getUsnid() == usuarioLogado.getUsnid() ? View.VISIBLE : View.GONE);
        } else { // O usuario NAO EH membro do grupo em questao
            holder.buttonAdicionarRemoverUsuario.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.buttonAdicionarRemoverUsuario.setText(context.getResources().getString(R.string.adapter_list_pesquisar_usuarios_button_adicionar_remover_usuario_adicionar));
            holder.buttonAdicionarRemoverUsuario.setVisibility(this.grupo.getAdmin().getUsnid() == this.usuarioLogado.getUsnid() ? View.VISIBLE : View.GONE);
        }
        holder.buttonAdicionarRemoverUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onCLick(view, position);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgressBar progressBar;
        ImageView imageAdmin;
        ImageView imageUSCFOTO;
        TextView textUSCNOME;
        TextView textUSCLOGN;
        TextView textUSCMAIL;
        Button buttonAdicionarRemoverUsuario;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            imageAdmin = (ImageView) view.findViewById(R.id.imageAdmin);
            imageUSCFOTO = (ImageView) view.findViewById(R.id.imageUSCFOTO);
            textUSCNOME = (TextView) view.findViewById(R.id.textUSCNOME);
            textUSCLOGN = (TextView) view.findViewById(R.id.textUSCLOGN);
            textUSCMAIL = (TextView) view.findViewById(R.id.textUSCMAIL);
            buttonAdicionarRemoverUsuario = (Button) view.findViewById(R.id.buttonAdicionarRemoverUsuario);
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
