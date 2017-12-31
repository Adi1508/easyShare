package com.example.aditya.easyshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri=data.getData();
            try{
                fixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                showImage.setImageBitmap(fixBitmap);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
