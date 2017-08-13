package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.model.Material;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */

public class PesquisarMateriaisFragmentAdapter extends RecyclerView.Adapter<PesquisarMateriaisFragmentAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<Material> materiais;
    private SimpleDateFormat dateFormat;

    public PesquisarMateriaisFragmentAdapter(Context context, List<Material> materiais) {
        this.context = context;
        this.materiais = materiais;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_pesquisar_materiais, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.materiais != null ? this.materiais.size() : 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textMACDESC.setText(this.materiais.get(position).getMacdesc());
        holder.textMADALDT.setText(this.dateFormat.format(this.materiais.get(position).getMadaldt()));
        holder.textMANQTDCE.setText(holder.textMANQTDCE.getText().toString().replace("XX", String.valueOf(this.materiais.get(position).getManqtdce())));
        holder.textMANQTDHA.setText(holder.textMANQTDHA.getText().toString().replace("XX", String.valueOf(this.materiais.get(position).getManqtdha())));
        if (this.materiais.get(position).getMactipo() == ETipoMaterial.APRESENTACAO) {
            holder.textMACTIPO.setText(ETipoMaterial.APRESENTACAO.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_power_point_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.EXERCICIO) {
            holder.textMACTIPO.setText(ETipoMaterial.EXERCICIO.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pdf_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.FORMULA) {
            holder.textMACTIPO.setText(ETipoMaterial.FORMULA.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_sigma_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.JOGO) {
            holder.textMACTIPO.setText(ETipoMaterial.JOGO.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_game_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.LIVRO) {
            holder.textMACTIPO.setText(ETipoMaterial.LIVRO.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_video_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.SIMULADO) {
            holder.textMACTIPO.setText(ETipoMaterial.SIMULADO.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_simulate_100px_gray));
        } else if (this.materiais.get(position).getMactipo() == ETipoMaterial.VIDEO) {
            holder.textMACTIPO.setText(ETipoMaterial.VIDEO.name().toString());
            holder.imageMACFOTO.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_video_100px_gray));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgressBar progressBar;
        ImageView imageMACFOTO;
        TextView textMACDESC;
        TextView textMACTIPO;
        TextView textMADALDT;
        TextView textMANQTDCE;
        TextView textMANQTDHA;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            imageMACFOTO = (ImageView) view.findViewById(R.id.imageMACFOTO);
            textMACDESC = (TextView) view.findViewById(R.id.textMACDESC);
            textMACTIPO = (TextView) view.findViewById(R.id.textMACTIPO);
            textMADALDT = (TextView) view.findViewById(R.id.textMADALDT);
            textMANQTDCE = (TextView) view.findViewById(R.id.textMANQTDCE);
            textMANQTDHA = (TextView) view.findViewById(R.id.textMANQTDHA);
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
