package com.example.thele.expired;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thele on 8/17/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Item> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView expDateView;
        public TextView purDateView;
        public Button buttonEdit;
        public Button buttonX;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name_column);
            expDateView = (TextView) itemView.findViewById(R.id.expdate);
            purDateView = (TextView) itemView.findViewById(R.id.purdate);
            buttonEdit = (Button) itemView.findViewById(R.id.buttonedit);
            buttonX = (Button) itemView.findViewById(R.id.buttonx);
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

    // TODO: Sort by purDate/expDate choice later
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
            final Item item = mDataset.get(position);
            holder.nameView.setText(item.getName());
            holder.expDateView.setText(Fridge.getFridge().printDate(item.getDateExpired()));
            holder.purDateView.setText(Fridge.getFridge().printDate(item.getDatePurchased()));
            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.buttonX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int pos = holder.getAdapterPosition();
                    mDataset.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos,mDataset.size());
                    Fridge.getFridge().removeItem(item);
                }
            });
    }

    public int getItemCount()
    {
        return mDataset.size();
    }
}
