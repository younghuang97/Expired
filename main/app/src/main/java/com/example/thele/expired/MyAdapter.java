package com.example.thele.expired;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by thele on 8/17/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private List<Item> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nameView;
        public TextView expDateView;
        public TextView purDateView;
        public Button tinyEditButton;
        public Button tinyDeleteButton;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name_column);
            expDateView = (TextView) itemView.findViewById(R.id.expdate);
            purDateView = (TextView) itemView.findViewById(R.id.purdate);
            tinyEditButton = (Button) itemView.findViewById(R.id.tinyeditbutton);
            tinyDeleteButton = (Button) itemView.findViewById(R.id.tinydeletebutton);
        }
    }

    public MyAdapter(List<Item> myDataset, Context context)
    {
        mDataset = myDataset;
        mContext = context;
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
            holder.tinyEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edititem, null);
                    mBuilder.setView(mView);
                    mBuilder.show();
                    EditText itemName = (EditText) mView.findViewById(R.id.etName);
                    EditText purDate = (EditText) mView.findViewById(R.id.etPurchaseDate);
                    EditText expDate = (EditText) mView.findViewById(R.id.etExpirationDate);
                    EditText storageType = (EditText) mView.findViewById(R.id.etStorageType);
                    Button editBtn = (Button) mView.findViewById(R.id.editbutton);
                    /*
                    itemName.setText();
                    purDate.setText();
                    expDate.setText();
                    storageType.setText();*/

                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //changes to item occur here
                        }
                    });

                }
            });
            holder.tinyDeleteButton.setOnClickListener(new View.OnClickListener() {
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
