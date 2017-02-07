package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sysmob.biblivirti.R;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class OpcoesGruposAdapter extends RecyclerView.Adapter<OpcoesGruposAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private String[] textOpcoes;
    private int[] imageOpcoes;

    public OpcoesGruposAdapter(Context context, String[] textOpcoes, int[] imageOpcoes) {
        this.context = context;
        this.textOpcoes = textOpcoes;
        this.imageOpcoes = imageOpcoes;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_opcoes_grupos, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.textOpcoes.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), this.imageOpcoes[position]));
        holder.text.setText(this.textOpcoes[position].toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image = (ImageView) view.findViewById(R.id.imageOpcao);
            text = (TextView) view.findViewById(R.id.textOpcao);
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
