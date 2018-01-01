package com.example.aditya.easyshare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 1/1/18.
 */

public class DisplayStorage extends AppCompatActivity {


    DatabaseReference databaseReference;

    RecyclerView recyclerView;

    RecyclerView.Adapter adapter;

    ProgressDialog progressDialog;

    List<ImageUploadInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_storage);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayStorage.this));

        progressDialog = new ProgressDialog(DisplayStorage.this);

        progressDialog.setMessage("Loading Images from Firebase Storage");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference(MainActivity.databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    System.out.println("ImageUploadINfo "+imageUploadInfo);
                    list.add(imageUploadInfo);
                }

                adapter= new RecyclerViewAdapter(getApplicationContext(),list);
                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
}
