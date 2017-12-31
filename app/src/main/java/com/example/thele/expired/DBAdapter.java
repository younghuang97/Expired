package com.example.thele.expired;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by thele on 8/17/2017.
 */

public class DBAdapter extends RecyclerView.Adapter<DBAdapter.ViewHolder>
{
    private String TAG = "DBAdapter";
    private List<PairOfDates> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nameView;
        public TextView fridgeDateView;
        public TextView freezerDateView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.itemName);
            fridgeDateView = (TextView) itemView.findViewById(R.id.fridgeDate);
            freezerDateView = (TextView) itemView.findViewById(R.id.freezerDate);
        }
    }

    public DBAdapter(List<PairOfDates> myDataset, Context context)
    {
        mDataset = myDataset;
        mContext = context;
    }

    public DBAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_db, parent, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final PairOfDates pair = mDataset.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_edit_date, null);
                final EditText itemName = (EditText) mView.findViewById(R.id.etName);
                final EditText fridgeDate = (EditText) mView.findViewById(R.id.etFridgeDate);
                final EditText freezerDate = (EditText) mView.findViewById(R.id.etFreezerDate);

                Button editBtn = (Button) mView.findViewById(R.id.editButton);
                Button deleteBtn = (Button) mView.findViewById(R.id.deleteButton);

                itemName.setText(holder.nameView.getText().toString().trim());
                fridgeDate.setText(holder.fridgeDateView.getText().toString().trim());
                freezerDate.setText(holder.freezerDateView.getText().toString().trim());

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mName = itemName.getText().toString().trim();
                        String mFridgeDate = fridgeDate.getText().toString().trim();
                        String mFreezerDate = freezerDate.getText().toString().trim();
                        PairOfDates mPair;

                        // no name
                        if (mName == null || mName.isEmpty()) {
                            //Display a toast or something "Please enter item name."
                            Toast toast = Toast.makeText(mContext, "Please enter item name.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        // no dates
                        else if ((mFridgeDate == null || mFridgeDate.isEmpty()) && (mFreezerDate == null || mFreezerDate.isEmpty())) {
                            Toast toast = Toast.makeText(mContext, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        // just freezer date
                        else if (mFridgeDate == null || mFridgeDate.isEmpty()) {
                            try {
                                pair.setName(mName);
                                pair.setFridge(0);
                                pair.setFreezer(parseInt(mFreezerDate));
                                notifyItemChanged(holder.getAdapterPosition());
                                Fridge.getFridge().writeDatabase(mContext);
                            }
                            catch (NumberFormatException e) {
                                Toast toast = Toast.makeText(mContext, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                        // just fridge date
                        else if (mFreezerDate == null || mFreezerDate.isEmpty()) {
                            try {
                                pair.setName(mName);
                                pair.setFridge(parseInt(mFridgeDate));
                                pair.setFreezer(0);
                                notifyItemChanged(holder.getAdapterPosition());
                                Fridge.getFridge().writeDatabase(mContext);
                            }
                            catch (NumberFormatException e) {
                                Toast toast = Toast.makeText(mContext, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                        // both dates
                        else {
                            try {
                                pair.setName(mName);
                                pair.setFridge(parseInt(mFridgeDate));
                                pair.setFreezer(parseInt(mFreezerDate));
                                notifyItemChanged(holder.getAdapterPosition());
                                Fridge.getFridge().writeDatabase(mContext);
                            }
                            catch (NumberFormatException e) {
                                Toast toast = Toast.makeText(mContext, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }

                        dialog.dismiss();
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        int pos = holder.getAdapterPosition();
                        mDataset.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos,mDataset.size());
                        Fridge.getFridge().removeExpDate(itemName.getText().toString().trim());
                        Fridge.getFridge().writeDatabase(mContext);
                        dialog.dismiss();
                    }
                });
            }
        });

        holder.nameView.setText(pair.getName());
        holder.fridgeDateView.setText(String.valueOf(pair.getFridge()));
        holder.freezerDateView.setText(String.valueOf(pair.getFreezer()));
    }

    public int getItemCount()
    {
        return mDataset.size();
    }
}
