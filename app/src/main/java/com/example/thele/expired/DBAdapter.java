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

    // TODO: Sort by purDate/expDate choice later
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final PairOfDates pair = mDataset.get(position);
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_edit_item, null);
                final EditText itemName = (EditText) mView.findViewById(R.id.etName);
                final EditText expDate = (EditText) mView.findViewById(R.id.etExpirationDate);
                final EditText purDate = (EditText) mView.findViewById(R.id.etPurchaseDate);
                final Spinner storageType = (Spinner) mView.findViewById(R.id.storageTypeSpinner);
                // sets a dropdown menu of certain options when editing storage type
                String[] storageTypes = mContext.getResources().getStringArray(R.array.storageTypes);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, storageTypes);
                storageType.setAdapter(adapter);
                Button editBtn = (Button) mView.findViewById(R.id.editbutton);
                Button deleteBtn = (Button) mView.findViewById(R.id.deletebutton);

                itemName.setText(holder.nameView.getText().toString().trim());
                expDate.setText(holder.fridgeDateView.getText().toString().trim());
                purDate.setText(holder.freezerDateView.getText().toString().trim());

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

                // opens a date picker
                purDate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
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
                            final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
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

                        else
                        {
                            holder.nameView.setText(name);
                            holder.fridgeDateView.setText(eDate);
                            holder.freezerDateView.setText(pDate);
                            // removes item, then puts its edited version back in
                            Fridge.getFridge().removeItem(item);
                            item.setName(name);
                            item.setDateExpired(Fridge.getFridge().printDate(eDate));
                            item.setDatePurchased(Fridge.getFridge().printDate(pDate));
                            item.setStorageType(sType);
                            Fridge.getFridge().addItem(item);
                            Fridge.getFridge().updateFridge(mContext);
                        }
                        // check for valid storage types.)

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
        */
        holder.nameView.setText(pair.getName());
        holder.fridgeDateView.setText(String.valueOf(pair.getFridge()));
        holder.freezerDateView.setText(String.valueOf(pair.getFreezer()));
    }

    public int getItemCount()
    {
        return mDataset.size();
    }
}
