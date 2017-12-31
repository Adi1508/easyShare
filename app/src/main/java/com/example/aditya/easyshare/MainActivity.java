package com.example.aditya.easyshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button getImage, uploadImage;
    ImageView showImage;
    EditText getImageName;
    Bitmap fixBitmap;
    String imageTag="image_tag";
    String imageName="image_data";

    String serverUploadPath="";

    ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String convertImage;
    String getImageFromEditText;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check=true;
    Uri uri;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        getImage = (Button)findViewById(R.id.display);
        uploadImage = (Button)findViewById(R.id.upload);
        showImage = (ImageView)findViewById(R.id.imageView);
        getImageName = (EditText)findViewById(R.id.editText1);

        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image From Gallery"), 1);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                getImageFromEditText = getImageName.getText().toString();
                uploadImageToServer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri=data.getData();
            try{
                System.out.println(uri);
                fixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                showImage.setImageBitmap(fixBitmap);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void uploadImageToServer(){

        StorageReference imageReference = mStorageRef.child(getImageFromEditText);
        //StorageReference imageRef = mStorageRef.child("images/"+getImageFromEditText);

        showImage.setDrawingCacheEnabled(true);
        showImage.buildDrawingCache();
        Bitmap bitmap= showImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData=baos.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println(downloadUrl);

                Toast.makeText(MainActivity.this,"The image is Uploaded", Toast.LENGTH_LONG).show();

                showImage.setImageResource(android.R.color.transparent);
                getImageName.getText().clear();
            }
        });
    }
}
