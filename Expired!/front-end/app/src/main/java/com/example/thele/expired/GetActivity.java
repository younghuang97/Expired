package com.example.thele.expired;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetActivity extends AppCompatActivity {
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
