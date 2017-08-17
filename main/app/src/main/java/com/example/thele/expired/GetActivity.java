package com.example.thele.expired;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class GetActivity extends AppCompatActivity {
    private static final String TAG = "GetActivity";
    Button button;
    EditText editText;
    TextView fridgeTxt;
    TextView frozenTxt;
    Fridge fridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        button = (Button) findViewById(R.id.btn);
        editText = (EditText) findViewById(R.id.findTxt);
        fridgeTxt = (TextView) findViewById(R.id.friTxt);
        frozenTxt = (TextView) findViewById(R.id.froTxt);
        fridge = new Fridge(GetActivity.this);
        fridge.readDatabase();
        fridge.readList();
        fridge.addExpDate(new PairOfDates(5, 16), "Carrot");
        fridge.addExpDate(new PairOfDates(2, 7), "Beef");
        fridge.addExpDate(new PairOfDates(2, 20), "Cabbage");
        fridge.addExpDate(new PairOfDates(3, 30), "Bread");
        fridge.writeDatabase();
    }

    public void buttonClick(View view) {
        String itemName = editText.getText().toString();
        Item item = new Item(itemName, "fridge");
        if (!fridge.addItem(item))
        {
            Toast toast = Toast.makeText(this, itemName + " has failed to be added.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            fridge.writeList();
            fridge.printDateExpired(5);
            Toast toast = Toast.makeText(this, itemName + " has been added. Set to expire on " + fridge.printDate(item.getDateExpired()), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
