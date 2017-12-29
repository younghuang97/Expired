package com.example.thele.expired;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class AddDateActivity extends AppCompatActivity {
    private static final String TAG = "AddDateActivity";
    Button button;
    EditText nameField;
    EditText fridgeDateField;
    EditText freezerDateField;
    String fontPath = "fonts/indie_flower.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);

        // get elements
        button = (Button) findViewById(R.id.addDateButton);
        nameField = (EditText) findViewById(R.id.nameField);
        fridgeDateField = (EditText) findViewById(R.id.fridgeDateField);
        freezerDateField = (EditText) findViewById(R.id.freezerDateField);

        // sets custom fonts
        Helper.setCustomFont(this, R.id.nameField, fontPath);
        Helper.setCustomFont(this, R.id.fridgeDateField, fontPath);
        Helper.setCustomFont(this, R.id.freezerDateField, fontPath);
    }

    public void buttonClick(View view) {
        String itemName = nameField.getText().toString().trim();
        String fridgeDate = fridgeDateField.getText().toString().trim();
        String freezerDate = freezerDateField.getText().toString().trim();

        PairOfDates pair;

        // no name
        if (itemName == null || itemName.isEmpty()) {
            //Display a toast or something "Please enter item name."
            Toast toast = Toast.makeText(this, "Please enter item name.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        // no dates
        else if ((fridgeDate == null || fridgeDate.isEmpty()) && (freezerDate == null || freezerDate.isEmpty())) {
            Toast toast = Toast.makeText(this, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        // just freezer date
        else if (fridgeDate == null || fridgeDate.isEmpty()) {
            try {
                pair = new PairOfDates(itemName, 0, parseInt(freezerDate));
                Fridge.getFridge().addExpDate(itemName, pair);
                Fridge.getFridge().writeDatabase(AddDateActivity.this);
                Toast toast = Toast.makeText(this, itemName + " has been added.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
            catch (NumberFormatException e) {
                Toast toast = Toast.makeText(this, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        // just fridge date
        else if (freezerDate == null || freezerDate.isEmpty()) {
            try {
                pair = new PairOfDates(itemName, parseInt(fridgeDate), 0);
                Fridge.getFridge().addExpDate(itemName, pair);
                Fridge.getFridge().writeDatabase(AddDateActivity.this);
                Toast toast = Toast.makeText(this, itemName + " has been added.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
            catch (NumberFormatException e) {
                Toast toast = Toast.makeText(this, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        // both dates
        else {
            try {
                pair = new PairOfDates(itemName, parseInt(fridgeDate), parseInt(freezerDate));
                Fridge.getFridge().addExpDate(itemName, pair);
                Fridge.getFridge().writeDatabase(AddDateActivity.this);
                Toast toast = Toast.makeText(this, itemName + " has been added.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
            catch (NumberFormatException e) {
                Toast toast = Toast.makeText(this, "Please enter at least one expiration date.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}