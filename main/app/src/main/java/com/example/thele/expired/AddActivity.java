package com.example.thele.expired;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    Button button;
    EditText nameField;
    EditText expDateField;
    EditText purDateField;
    Spinner storageTypeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        button = (Button) findViewById(R.id.add_button);
        nameField = (EditText) findViewById(R.id.nameField);
        expDateField = (EditText) findViewById(R.id.expDateField);
        purDateField = (EditText) findViewById(R.id.purDateField);
        storageTypeField = (Spinner) findViewById(R.id.storage_type_spinner);
        // sets a dropdown menu of certain options when editing storage type
        String[] storageTypes = getResources().getStringArray(R.array.storage_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storageTypes);
        storageTypeField.setAdapter(adapter);

        // opens a date picker
        purDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(purDateField.getText().toString().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog dpd = new DatePickerDialog(AddActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int month, int day) {
                                    try {
                                        // updates data field
                                        String sDate = Integer.toString(month + 1) + '/' + day + '/' + year;
                                        Date date = sdf.parse(sDate);
                                        purDateField.setText(sdf.format(date));
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
        expDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(expDateField.getText().toString().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog dpd = new DatePickerDialog(AddActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int month, int day) {
                                    try {
                                        // updates data field
                                        String sDate = Integer.toString(month + 1) + '/' + day + '/' + year;
                                        Date date = sdf.parse(sDate);
                                        expDateField.setText(sdf.format(date));
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
    }

    public void buttonClick(View view) {
        String itemName = nameField.getText().toString().trim();
        String expDate = expDateField.getText().toString().trim();
        String purDate = purDateField.getText().toString().trim();
        String storageType = storageTypeField.getSelectedItem().toString().trim();
        String expDate2 = Fridge.getFridge().printDate(expDate); // used to create the item
        String purDate2 = Fridge.getFridge().printDate(purDate); // used to create the item

        Item item = null;

        if (itemName.length() == 0) {
            //Display a toast or something "Please enter item name."
            Toast toast = Toast.makeText(this, "Please enter item name.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (purDate.length() == 0)
        {
            if (expDate.length() == 0) {
                item = new Item(itemName, storageType);
            }
            else
            {
                item = new Item(itemName, expDate2, storageType);
            }
        }
        else
        {
            if (expDate.length() == 0) {
                item = new Item(itemName, storageType);
                item.setDatePurchased(purDate2);
            }
            else
            {
                item = new Item(itemName, purDate2, expDate2, storageType);
            }
        }
        if (item != null)
        {
            if (!Fridge.getFridge().addItem(item)) {
                Toast toast = Toast.makeText(this, itemName + " has failed to be added.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Fridge.getFridge().writeList(AddActivity.this);
                Toast toast = Toast.makeText(this, itemName + " has been added. Set to expire on " + expDate, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        }
    }
}
