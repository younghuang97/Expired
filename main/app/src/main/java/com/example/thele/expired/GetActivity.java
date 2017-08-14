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

import java.util.Calendar;

public class GetActivity extends AppCompatActivity {
    private static final String TAG = "GetActivity";
    Button button;
    EditText editText;
    TextView fridgeTxt;
    TextView frozenTxt;
    String item_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        button = (Button)findViewById(R.id.btn);
        editText = (EditText)findViewById(R.id.findTxt);
        fridgeTxt = (TextView)findViewById(R.id.friTxt);
        frozenTxt = (TextView)findViewById(R.id.froTxt);
        final Fridge fridge = new Fridge();
        fridge.readDatabase();
        //fridge.readList();
        //ridge.printDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item(editText.getText().toString(), "20170327", "20170330", "fridge");
                fridge.addItem(item);
                fridge.writeDatabase();
                fridge.writeList();
            }
        });
    }
}
