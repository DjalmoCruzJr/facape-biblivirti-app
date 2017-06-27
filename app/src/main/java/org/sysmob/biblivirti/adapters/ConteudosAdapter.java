package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.model.Conteudo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class ConteudosAdapter extends RecyclerView.Adapter<ConteudosAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;
    private List<Conteudo> conteudos;
    private SimpleDateFormat dateFormat;

    public ConteudosAdapter(Context context, List<Conteudo> conteudos) {
        this.context = context;
        this.conteudos = conteudos;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_conteudos, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.conteudos.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textCOCDESC.setText(this.conteudos.get(position).getCocdesc().toString());
        holder.textCODALDT.setText(this.dateFormat.format(this.conteudos.get(position).getCodaldt()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textCOCDESC;
        TextView textCODALDT;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            textCOCDESC = (TextView) view.findViewById(R.id.textCOCDESC);
            textCODALDT = (TextView) view.findViewById(R.id.textCODALDT);
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
