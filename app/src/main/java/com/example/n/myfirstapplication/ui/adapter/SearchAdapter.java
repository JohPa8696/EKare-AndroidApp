package com.example.n.myfirstapplication.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.n.myfirstapplication.ItemInListView;
import com.example.n.myfirstapplication.R;


import java.util.ArrayList;

/**
 * Created by n on 22/07/2017.
 */

// ToDo try to use base adapter?
public class SearchAdapter extends BaseAdapter implements Filterable{
    private ArrayList<ItemInListView> items; // list used by array adapter
    private ArrayList<ItemInListView> allItems; // list used to store all users
    private ArrayList<ItemInListView> suggestions; // list to store suggestions
    private Context mContext;


    public SearchAdapter(Context context, ArrayList<ItemInListView> items){
        this.mContext = context;
        this.items = items;
        this.suggestions = new ArrayList<>();
        this.allItems = (ArrayList<ItemInListView>) items.clone();
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ItemInListView item = (ItemInListView) getItem(position);
        Log.d("adapter", "in here!");

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_in_list_search, parent, false);

            holder.nameText = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.emailText = (TextView) convertView.findViewById(R.id.emailTextView);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameText.setText(item.getName());
        holder.emailText.setText(item.getEmail());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    Filter mFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue){
            ItemInListView item = (ItemInListView) resultValue;
            return item.name;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null){
                suggestions.clear();
                for(ItemInListView i : allItems){
                    if (i.toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(i);
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //ArrayList<ItemInListView> list = (ArrayList<ItemInListView>) results.values;
            if(results != null && results.count > 0){
                items = (ArrayList<ItemInListView>) results.values;
                notifyDataSetChanged();
            }else{
                items.clear();
                notifyDataSetChanged();
            }
        }
    };

    public static class ViewHolder{
        public TextView nameText;
        public TextView emailText;
    }
}
