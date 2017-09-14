package com.example.thele.expired;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    Button button;
    EditText nameField;
    EditText expDateField;
    EditText storageTypeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        button = (Button) findViewById(R.id.add_button);
        nameField = (EditText) findViewById(R.id.nameField);
        expDateField = (EditText) findViewById(R.id.expDateField);
        storageTypeField = (EditText) findViewById(R.id.storageTypeField);
    }

    public void buttonClick(View view) {
        String itemName = nameField.getText().toString().trim();
        String expDate = expDateField.getText().toString().trim();
        String storageType = storageTypeField.getText().toString().trim().toLowerCase();
        Item item = null;

        if (itemName.length() == 0) {
            //Display a toast or something "Please enter item name."
            Toast toast = Toast.makeText(this, "1", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (storageType.length() == 0) {
            //Display a toast or something "Please enter a storage type."
            Toast toast = Toast.makeText(this, "2", Toast.LENGTH_SHORT);
            toast.show();
        }
        // TODO: Implement a spinner later instead of an EditText
        else if (!storageType.equals("Fridge") && !storageType.equals("Freezer") && !storageType.equals("Misc")) {
            //Display a toast or something "Please enter a valid storage type or enter "misc"."
            Toast toast = Toast.makeText(this, storageType, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (expDate.length() == 0) {
            item = new Item(itemName, storageType);
        }
        else
        {
            item = new Item(itemName, expDate, storageType);
        }
        if (item != null)
        {
            if (!Fridge.getFridge().addItem(item)) {
                Toast toast = Toast.makeText(this, itemName + " has failed to be added.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Fridge.getFridge().writeList(AddActivity.this);
                Toast toast = Toast.makeText(this, itemName + " has been added. Set to expire on " + Fridge.getFridge().printPrettyDate(item.getDateExpired()), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
