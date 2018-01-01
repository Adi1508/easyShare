package com.example.aditya.easyshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aditya on 1/1/18.
 */

public class ImageProcessing extends AppCompatActivity {

    TextView mTextView;
    String key = "";
    String secret = "";
    String imageURL;
    StringBuffer sb = new StringBuffer();
    JSONObject jsonObject;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);

        mTextView = (TextView) findViewById(R.id.mTextView);

        RequestQueue queue = Volley.newRequestQueue(this);
        String api = "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    jsonObject=new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("JSON: "+jsonObject.toString());
                mTextView.setText("Response is:  " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: "+error);
                mTextView.setText("api didn't work");
            }
        });
        queue.add(stringRequest);
    }
}
