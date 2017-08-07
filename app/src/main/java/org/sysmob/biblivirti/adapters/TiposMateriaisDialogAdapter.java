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

import java.util.List;

/**
 * Created by micro12 on 26/06/2017.
 */

public class TiposMateriaisDialogAdapter extends RecyclerView.Adapter<TiposMateriaisDialogAdapter.ViewHolder> {

    private Context context;
    private List<ETipoMaterial> tiposMateriais;
    private TiposMateriaisDialogAdapter.OnItemClickListener onItemClickListener;

    public TiposMateriaisDialogAdapter(Context context, List<ETipoMaterial> tiposMateriais) {
        this.context = context;
        this.tiposMateriais = tiposMateriais;
    }

    public TiposMateriaisDialogAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(TiposMateriaisDialogAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_tipos_materiais, parent, false);
        TiposMateriaisDialogAdapter.ViewHolder viewHolder = new TiposMateriaisDialogAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(this.tiposMateriais.get(position).name());
        if (this.tiposMateriais.get(position) == ETipoMaterial.APRESENTACAO) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_power_point_50px_blue));
        } else if (this.tiposMateriais.get(position) == ETipoMaterial.EXERCICIO) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pdf_50px_blue));
        } else if (this.tiposMateriais.get(position) == ETipoMaterial.FORMULA) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_sigma_50px_blue));
        } else if (this.tiposMateriais.get(position) == ETipoMaterial.JOGO) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_game_50px_blue));
        } else if (this.tiposMateriais.get(position) == ETipoMaterial.LIVRO) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_book_50px_blue));
        } else if (this.tiposMateriais.get(position) == ETipoMaterial.SIMULADO) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_simulate_50px_blue));
        } else if (this.tiposMateriais.get(position) == ETipoMaterial.VIDEO) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_video_50px_blue));
        }
    }

    @Override
    public int getItemCount() {
        return this.tiposMateriais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image = (ImageView) view.findViewById(R.id.imageTipoMaterial);
            text = (TextView) view.findViewById(R.id.textTipoMaterial);
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
