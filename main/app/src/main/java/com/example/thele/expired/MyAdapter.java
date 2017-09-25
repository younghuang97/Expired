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
import android.widget.AutoCompleteTextView;
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
            holder.expDateView.setText(Fridge.getFridge().printPrettyDate(item.getDateExpired()));
            holder.purDateView.setText(Fridge.getFridge().printPrettyDate(item.getDatePurchased()));
            holder.tinyEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edititem, null);
                    final EditText itemName = (EditText) mView.findViewById(R.id.etName);
                    final EditText purDate = (EditText) mView.findViewById(R.id.etPurchaseDate);
                    final EditText expDate = (EditText) mView.findViewById(R.id.etExpirationDate);
                    final Spinner storageType = (Spinner) mView.findViewById(R.id.storage_type_spinner);
                    // sets a dropdown menu of certain options when editing storage type
                    String[] storageTypes = mContext.getResources().getStringArray(R.array.storage_types);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, storageTypes);
                    storageType.setAdapter(adapter);
                    Button editBtn = (Button) mView.findViewById(R.id.editbutton);

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
                                holder.expDateView.setText(eDate);
                                holder.purDateView.setText(pDate);
                                // removes item, then puts its edited version back in
                                Fridge.getFridge().removeItem(item);
                                item.setName(name);
                                item.setDateExpired(Fridge.getFridge().printDate(eDate));
                                item.setDatePurchased(Fridge.getFridge().printDate(pDate));
                                item.setStorageType(sType);
                                Fridge.getFridge().addItem(item);
                                Fridge.getFridge().writeList(mContext);
                            }
                            // check for valid storage types.)

                            dialog.dismiss();
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
                    Fridge.getFridge().writeList(mContext);
                }
            });
    }

    public int getItemCount()
    {
        return mDataset.size();
    }
}
