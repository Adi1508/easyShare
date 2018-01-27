package com.example.aditya.easyshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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

    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth=FirebaseAuth.getInstance();
        /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accesskey", 0);

        if (sharedPreferences.getInt("access", 0) == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("access", 0);
            editor.commit();
        }*/

        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this, LandingActivity.class));
                    finish();
                }else{
                    Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                //SharedPreferences sharedPreferences=getSharedPreferences("accesskey",0);
//                if(sharedPreferences.getInt("access",0)==0){
//                    System.out.println("not logged in");
//                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }else{
//                    System.out.println("logged in");
//                    Intent intent=new Intent(MainActivity.this, LandingActivity.class);
//                    startActivity(intent);
//                }


            }
        }.start();
    }
}
