package com.example.thele.expired;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by thele on 8/17/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Item> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView expDateView;
        public TextView purDateView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            expDateView = (TextView) itemView.findViewById(R.id.expdate);
            purDateView = (TextView) itemView.findViewById(R.id.purdate);
        }
    }

    public MyAdapter(List<Item> myDataset) {
        mDataset = myDataset;
    }

    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent, false);

        return new ViewHolder(v);
    }

    // TODO: Sort by type
    public void onBindViewHolder(ViewHolder holder, int position)
    {
            Item item = mDataset.get(position);
            holder.nameView.setText(item.getName());
            holder.expDateView.setText(Fridge.getFridge().printDate(item.getDateExpired()));
            holder.purDateView.setText(Fridge.getFridge().printDate(item.getDatePurchased()));
    }

    public int getItemCount()
    {
        return mDataset.size();
    }
}
