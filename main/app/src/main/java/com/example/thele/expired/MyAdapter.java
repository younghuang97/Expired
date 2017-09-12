package com.example.thele.expired;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private String TAG = "MyAdapter";
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
                    final EditText itemName = (EditText) mView.findViewById(R.id.etName);
                    final EditText purDate = (EditText) mView.findViewById(R.id.etPurchaseDate);
                    final EditText expDate = (EditText) mView.findViewById(R.id.etExpirationDate);
                    final EditText storageType = (EditText) mView.findViewById(R.id.etStorageType);
                    Button editBtn = (Button) mView.findViewById(R.id.editbutton);

                    itemName.setText(holder.nameView.getText().toString().trim());
                    purDate.setText(holder.purDateView.getText().toString().trim());
                    expDate.setText(holder.expDateView.getText().toString().trim());
                    storageType.setText(item.getStorageType());

                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = itemName.getText().toString().trim();
                            String pDate = purDate.getText().toString().trim();
                            String eDate = expDate.getText().toString().trim();
                            String sType = storageType.getText().toString().trim();

                            // checks for empty fields
                            String emptyStringMessage = "The following fields are empty: ";
                            Toast emptyToast = Toast.makeText(mContext, emptyStringMessage, Toast.LENGTH_LONG);
                            boolean empty = false;
                            boolean firstEmpty = true; // used to track when to add a comma
                            if (name.isEmpty())
                            {
                                firstEmpty = false;
                                emptyStringMessage += "Name";
                                emptyToast.setText(emptyStringMessage);
                                empty = true;
                            }
                            if (pDate.isEmpty())
                            {
                                if (!firstEmpty)
                                {
                                    emptyStringMessage += ", Purchase Date";
                                }
                                else
                                {
                                    emptyStringMessage += "Purchase Date";
                                    firstEmpty = false;
                                }
                                emptyToast.setText(emptyStringMessage);
                                empty = true;
                            }
                            if (eDate.isEmpty())
                            {
                                if (!firstEmpty)
                                {
                                    emptyStringMessage += ", Expiration Date";
                                }
                                else
                                {
                                    emptyStringMessage += "Expiration Date";
                                    firstEmpty = false;
                                }
                                emptyToast.setText(emptyStringMessage);
                                empty = true;
                            }
                            if (sType.isEmpty())
                            {
                                if (!firstEmpty)
                                {
                                    emptyStringMessage += ", Storage Type";
                                }
                                else
                                {
                                    emptyStringMessage += "Storage Type";
                                }
                                emptyToast.setText(emptyStringMessage);
                                empty = true;
                            }
                            // for proper grammar
                            emptyStringMessage += '.';
                            emptyToast.setText(emptyStringMessage);

                            if (empty)
                            {
                                emptyToast.show();
                            }


                            // check for valid date(exp date should be at least after today's date)
                            else
                            {

                            }
                            // check for valid storage types.)

                            // replaces the item's details where applicable

                            // update main activity to reflect changes
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
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
