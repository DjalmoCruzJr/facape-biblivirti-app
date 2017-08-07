package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.model.Conteudo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class ConteudosRelacionadosAdapter extends RecyclerView.Adapter<ConteudosRelacionadosAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<Conteudo> conteudos;
    private SimpleDateFormat dateFormat;

    public ConteudosRelacionadosAdapter(Context context, List<Conteudo> conteudos) {
        this.context = context;
        this.conteudos = conteudos;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_conteudos_relacionados, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.conteudos.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textCOCDESC.setText(this.conteudos.get(position).getCocdesc().toString());
        holder.checkSelecionado.setSelected(this.conteudos.get(position).isSelected());
        holder.checkSelecionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onCLick(view, position);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textCOCDESC;
        CheckBox checkSelecionado;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textCOCDESC = (TextView) view.findViewById(R.id.textCOCDESC);
            checkSelecionado = (CheckBox) view.findViewById(R.id.checkSelecionado);
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
