package com.example.thele.expired;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";
    Button button;
    EditText nameField;
    EditText expDateField;
    EditText purDateField;
    Spinner storageTypeField;
    String fontPath = "fonts/indie_flower.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        // get elements
        button = (Button) findViewById(R.id.addDatesButton);
        nameField = (EditText) findViewById(R.id.nameField);
        expDateField = (EditText) findViewById(R.id.expirationDateField);
        purDateField = (EditText) findViewById(R.id.purchaseDateField);
        purDateField.setText(sdf.format(new Date()));
        storageTypeField = (Spinner) findViewById(R.id.storageTypeSpinner);

        // sets custom fonts
        Helper.setCustomFont(this, R.id.nameField, fontPath);
        Helper.setCustomFont(this, R.id.expirationDateField, fontPath);
        Helper.setCustomFont(this, R.id.purchaseDateField, fontPath);

        // sets a dropdown menu of certain options when editing storage type
        String[] storageTypes = getResources().getStringArray(R.array.storageTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storageTypes);
        storageTypeField.setAdapter(adapter);

        // opens a date picker
        purDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(purDateField.getText().toString().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog dpd = new DatePickerDialog(AddItemActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int month, int day) {
                                    // updates data field
                                    String purchaseDate = Integer.toString(month + 1) + '/' + day + '/' + (year%100);
                                    purDateField.setText(purchaseDate);
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
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(expDateField.getText().toString().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog dpd = new DatePickerDialog(AddItemActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int month, int day) {
                                    // updates data field
                                    String expirationDate = Integer.toString(month + 1) + '/' + day + '/' + (year%100);
                                    expDateField.setText(expirationDate);
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

        Item item;

        // no name
        if (itemName == null || itemName.isEmpty()) {
            //Display a toast or something "Please enter item name."
            Toast toast = Toast.makeText(this, "Please enter item name.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        // if no expiration date, search db for default, if not in db, display an error message
        else if (expDate == null || expDate.isEmpty()) {
            item = new Item(itemName, storageType);
            if (!Fridge.getFridge().addItem(item)) {
                Toast toast = Toast.makeText(this, "Please enter an expiration date.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Fridge.getFridge().updateFridge(this);
                Toast toast = Toast.makeText(this, itemName + " has been added. Set to expire on " + expDate, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }

        }
        // if all the fields are filled out, make an item with the fields
        else {
            item = new Item(itemName, purDate2, expDate2, storageType);
            if (!Fridge.getFridge().addItem(item)) {
                Toast toast = Toast.makeText(this, itemName + " has failed to be added.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Fridge.getFridge().updateFridge(this);
                Toast toast = Toast.makeText(this, itemName + " has been added. Set to expire on " + expDate, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        }
    }
}