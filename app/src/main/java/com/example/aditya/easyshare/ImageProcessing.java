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
    String key = "THO9NVu2F9be4zenaJvgeC435hdziNl0";
    String secret = "gM_p6nlgPlmMFcMSSfkISxhkmm_uYaKM";
    String imageURL;
    StringBuffer sb = new StringBuffer();
    JSONObject jsonObject;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);

        mTextView = (TextView) findViewById(R.id.mTextView);

        RequestQueue queue = Volley.newRequestQueue(this);
        String api = "https://api-us.faceplusplus.com/facepp/v3/detect?api_key=THO9NVu2F9be4zenaJvgeC435hdziNl0&api_secret=gM_p6nlgPlmMFcMSSfkISxhkmm_uYaKM&image_url=https://firebasestorage.googleapis.com/v0/b/kotlinapp-1508.appspot.com/o/groot?alt=media%26token=5c493a46-efa4-4f80-8120-14c5392093ab&return_attributes=gender";

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
