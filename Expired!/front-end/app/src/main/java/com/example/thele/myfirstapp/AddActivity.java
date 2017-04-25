package com.example.thele.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    TextView fridgeTxt;
    TextView frozenTxt;
    String item_name;
    String server_url = "http://128.54.238.234/query.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        button = (Button)findViewById(R.id.btn);
        editText = (EditText)findViewById(R.id.findTxt);
        fridgeTxt = (TextView)findViewById(R.id.friTxt);
        frozenTxt = (TextView)findViewById(R.id.froTxt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item_name = editText.getText().toString();
                if (item_name.equals(""))
                {
                    fridgeTxt.setText("No input.");
                    frozenTxt.setText("");
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String fridge = jsonObject.getString("fridge");
                                        if (fridge.equals("null"))
                                        {
                                            fridgeTxt.setText("Item isn't stored in database.");
                                            frozenTxt.setText("");
                                        }
                                        else {
                                            String frozen = jsonObject.getString("frozen");
                                            fridge = fridge + " days refrigerated.";
                                            frozen = frozen + " days frozen.";
                                            fridgeTxt.setText(fridge);
                                            frozenTxt.setText(frozen);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            fridgeTxt.setText("Something went wrong...");
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", item_name);

                            return params;
                        }
                    };
                    MySingleton.getInstance(AddActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }
}
