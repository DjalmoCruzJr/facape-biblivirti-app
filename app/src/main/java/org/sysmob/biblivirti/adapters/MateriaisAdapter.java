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
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.model.Material;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class MateriaisAdapter extends RecyclerView.Adapter<MateriaisAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;
    private List<Material> materiais;
    private SimpleDateFormat dateFormat;

    public MateriaisAdapter(Context context, List<Material> materiais) {
        this.context = context;
        this.materiais = materiais;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_materiais, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.materiais.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textMACTIPO.setText(this.materiais.get(position).getMactipo().name());
        holder.textMADALDT.setText(this.dateFormat.format(this.materiais.get(position).getMadaldt()));
        holder.textMACDESC.setText(this.materiais.get(position).getMacdesc());
        holder.textMANQTDCE.setText(holder.textMANQTDCE.getText().toString().replace("XX", String.valueOf(this.materiais.get(position).getManqtdce())));
        holder.textMANQTDHA.setText(holder.textMANQTDHA.getText().toString().replace("XX", String.valueOf(this.materiais.get(position).getManqtdha())));
        if (this.materiais.get(position).getMactipo() == ETipoMaterial.APRESENTACAO) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_power_point_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.EXERCICIO) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pdf_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.FORMULA) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_sigma_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.JOGO) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_game_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.LIVRO) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_video_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.SIMULADO) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_simulate_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.VIDEO) {
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_video_100px_gray));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textMACTIPO;
        TextView textMADALDT;
        TextView textMACDESC;
        TextView textMANQTDCE;
        TextView textMANQTDHA;
        ImageView imageMACFOTO;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            textMACTIPO = (TextView) view.findViewById(R.id.textMACTIPO);
            textMADALDT = (TextView) view.findViewById(R.id.textMADALDT);
            textMACDESC = (TextView) view.findViewById(R.id.textMACDESC);
            textMANQTDCE = (TextView) view.findViewById(R.id.textMANQTDCE);
            textMANQTDHA = (TextView) view.findViewById(R.id.textMANQTDHA);
            imageMACFOTO = (ImageView) view.findViewById(R.id.imageMACFOTO);
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
