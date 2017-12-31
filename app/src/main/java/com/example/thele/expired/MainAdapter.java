package com.example.thele.expired;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by thele on 8/17/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
{
    private String TAG = "MainAdapter";
    private List<Item> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nameView;
        public TextView expDateView;
        public TextView purDateView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.itemName);
            expDateView = (TextView) itemView.findViewById(R.id.expirationDate);
            purDateView = (TextView) itemView.findViewById(R.id.purchaseDate);
        }
    }

    public MainAdapter(List<Item> myDataset, Context context)
    {
        mDataset = myDataset;
        mContext = context;
    }

    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_main, parent, false);
        return new ViewHolder(v);
    }

    // TODO: Sort by purDate/expDate choice later
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final Item item = mDataset.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_edit_item, null);
                final EditText itemName = (EditText) mView.findViewById(R.id.etName);
                final EditText purDate = (EditText) mView.findViewById(R.id.etFridgeDate);
                final EditText expDate = (EditText) mView.findViewById(R.id.etFreezerDate);
                final Spinner storageType = (Spinner) mView.findViewById(R.id.storageTypeSpinner);
                // sets a dropdown menu of certain options when editing storage type
                String[] storageTypes = mContext.getResources().getStringArray(R.array.storageTypes);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, storageTypes);
                storageType.setAdapter(adapter);
                Button editBtn = (Button) mView.findViewById(R.id.editButton);
                Button deleteBtn = (Button) mView.findViewById(R.id.deleteButton);

                itemName.setText(holder.nameView.getText().toString().trim());
                purDate.setText(holder.purDateView.getText().toString().trim());
                expDate.setText(holder.expDateView.getText().toString().trim());
                final String storeType = item.getStorageType();
                if (storeType.equals("Fridge"))
                {
                    storageType.setSelection(0);
                }
                else if (storeType.equals("Freezer"))
                {
                    storageType.setSelection(1);
                }
                else if (storeType.equals("Misc."))
                {
                    storageType.setSelection(2);
                }

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

                // opens a date picker
                purDate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            Calendar cal = Calendar.getInstance();
                            try {
                                cal.setTime(sdf.parse(purDate.getText().toString().trim()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            DatePickerDialog dpd = new DatePickerDialog(mContext,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int month, int day) {
                                            try {
                                                // updates data field
                                                String sDate = Integer.toString(month + 1) + '/' + day + '/' + year;
                                                Date date = sdf.parse(sDate);
                                                purDate.setText(sdf.format(date));
                                            } catch (ParseException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.show();
                        }
                        return true;
                    }
                });

                // opens a date picker
                expDate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            Calendar cal = Calendar.getInstance();
                            try {
                                cal.setTime(sdf.parse(expDate.getText().toString().trim()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            DatePickerDialog dpd = new DatePickerDialog(mContext,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int month, int day) {
                                            try {
                                                // updates data field
                                                String sDate = Integer.toString(month + 1) + '/' + day + '/' + year;
                                                Date date = sdf.parse(sDate);
                                                expDate.setText(sdf.format(date));
                                            } catch (ParseException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.show();
                        }
                        return true;
                    }
                });

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = itemName.getText().toString().trim();
                        String pDate = purDate.getText().toString().trim();
                        String eDate = expDate.getText().toString().trim();
                        String sType = storageType.getSelectedItem().toString().trim();

                        // checks for empty fields
                        String missingFieldsMessage = "The following fields are empty: ";
                        Toast missingFieldsToast = Toast.makeText(mContext, missingFieldsMessage, Toast.LENGTH_LONG);
                        boolean empty = true;
                        boolean firstEmpty = true; // used to track when to add a comma
                        if (name.isEmpty())
                        {
                            firstEmpty = false;
                            missingFieldsMessage += "Name";
                            missingFieldsToast.setText(missingFieldsMessage);
                            empty = false;
                        }
                        if (pDate.isEmpty())
                        {
                            if (!firstEmpty)
                            {
                                missingFieldsMessage += ", Purchase Date";
                            }
                            else
                            {
                                missingFieldsMessage += "Purchase Date";
                                firstEmpty = false;
                            }
                            missingFieldsToast.setText(missingFieldsMessage);
                            empty = false;
                        }
                        if (eDate.isEmpty())
                        {
                            if (!firstEmpty)
                            {
                                missingFieldsMessage += ", Expiration Date";
                            }
                            else
                            {
                                missingFieldsMessage += "Expiration Date";
                            }
                            missingFieldsToast.setText(missingFieldsMessage);
                            empty = false;
                        }

                        // for proper grammar
                        missingFieldsMessage += '.';
                        missingFieldsToast.setText(missingFieldsMessage);

                        if (!empty)
                        {
                            missingFieldsToast.show();
                        }
                        else
                        {
                            item.setName(name);
                            item.setDateExpired(Fridge.getFridge().printDate(eDate));
                            item.setDatePurchased(Fridge.getFridge().printDate(pDate));
                            item.setStorageType(sType);
                            notifyItemChanged(holder.getAdapterPosition());
                            Fridge.getFridge().updateFridge(mContext);
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
                        Fridge.getFridge().removeItem(item);
                        Fridge.getFridge().updateFridge(mContext);
                        dialog.dismiss();
                    }
                });
            }
        });

        holder.nameView.setText(item.getName());
        holder.expDateView.setText(Fridge.getFridge().printPrettyDate(item.getDateExpired()));
        holder.purDateView.setText(Fridge.getFridge().printPrettyDate(item.getDatePurchased()));
    }
/*
    public void filter(String text) {
        items.clear();
        if(text.isEmpty()){
            items.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(PhoneBookItem item: itemsCopy){
                if(item.name.toLowerCase().contains(text) || item.phone.toLowerCase().contains(text)){
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
*/
    public int getItemCount()
    {
        return mDataset.size();
    }
}
