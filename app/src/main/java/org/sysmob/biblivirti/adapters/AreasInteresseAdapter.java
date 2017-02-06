package org.sysmob.biblivirti.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.sysmob.biblivirti.model.AreaInteresse;

import java.util.List;

/**
 * Created by micro99 on 06/02/2017.
 */

public class AreasInteresseAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<AreaInteresse> areasInteresse;

    public AreasInteresseAdapter(Context context, List<AreaInteresse> areasInteresse) {
        this.context = context;
        this.areasInteresse = areasInteresse;
    }

    @Override
    public int getCount() {
        return this.areasInteresse.size();
    }

    @Override
    public Object getItem(int position) {
        return this.areasInteresse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView text = (TextView) view.findViewById(android.R.id.text1);
        text.setText(this.areasInteresse.get(position).getAicdesc().toString());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence text) {
                FilterResults filterResults = new FilterResults();
                if (text != null) {
                    filterResults.values = areasInteresse;
                    filterResults.count = areasInteresse.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    areasInteresse = (List<AreaInteresse>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
