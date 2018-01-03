package com.example.aditya.easyshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 1/1/18.
 */

public class ImageProcessing extends AppCompatActivity {

    ListView listView;
    String key = "THO9NVu2F9be4zenaJvgeC435hdziNl0";
    String secret = "gM_p6nlgPlmMFcMSSfkISxhkmm_uYaKM";
    String imageURL;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String api;
    FirebaseDatabase firebaseDatabase;
    List<String> imagesInfoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);

        listView=findViewById(R.id.listInfo);
        final RequestQueue queue = Volley.newRequestQueue(this);

        firebaseDatabase= FirebaseDatabase.getInstance();

        final Query query= firebaseDatabase.getReference().child("uploaded_images_info");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null){
                    final String imageName=String.valueOf(dataSnapshot.child("imageName").getValue());
                    imageURL=String.valueOf(dataSnapshot.child("imageURL").getValue());
                    System.out.println(imageName+" "+imageURL);
                    api="https://api-us.faceplusplus.com/facepp/v3/detect?api_key="+key+"&api_secret="+secret+"&image_url="+imageURL+"&return_attributes=gender";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonObject = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                jsonArray=jsonObject.getJSONArray("faces");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String listData=imageName+" Number of faces detected: " + jsonArray.length();
                            System.out.println(listData);
                            imagesInfoList.add(listData);

                            ArrayAdapter adapter=new ArrayAdapter<String>(ImageProcessing.this, R.layout.activity_listview,imagesInfoList);
                            listView.setAdapter(adapter);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("api did't work "+error);
                        }
                    });
                    queue.add(stringRequest);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
