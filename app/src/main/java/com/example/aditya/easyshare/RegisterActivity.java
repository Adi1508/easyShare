package com.example.aditya.easyshare;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.net.Uri;

import com.example.aditya.easyshare.ImageCompression;

/**
 * Created by aditya on 27/1/18.
 */


public class RegisterActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    public EditText name, email, password;
    private Button register, login, select;
    ProgressBar progressBar;
    public ImageView imageView;
    private Uri uri;
    private Bitmap fixBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        System.out.println("LoginActivity Started");

        firebaseAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        //select = findViewById(R.id.select_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar);
        password = findViewById(R.id.pass);
        imageView = findViewById(R.id.face);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = email.getText().toString().trim();
                String inputPass = password.getText().toString().trim();

                if (TextUtils.isEmpty(inputEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(inputPass)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputPass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPass)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(RegisterActivity.this, LandingActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                System.out.println("URI: "+ uri);
                //Bitmap pic= (Bitmap) data.getExtras().get("data");
                //String mDrawableName="myImage";
                //int resID=getResources().getIdentifier(mDrawableName, "drawable",getPackageName());

                //the image is set to image view
                fixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap bitmap = fixBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                /*Uri tempUri = getImageUri(getApplicationContext(), pic);
                File finalFile = new File(getRealPathFromUri(tempUri));
                Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(finalFile));
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                Log.e("Screen width ", " "+width);
                Log.e("Screen height ", " "+height);
                Log.e("img width ", " "+myBitmap.getWidth());
                Log.e("img height ", " "+myBitmap.getHeight());
                float scaleHt =(float) width/myBitmap.getWidth();
                Log.e("Scaled percent ", " "+scaleHt);
                Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, width, (int)(myBitmap.getWidth()*scaleHt), true);
                imageView.setImageBitmap(scaled);*/

                /*BitmapFactory.Options opts=new BitmapFactory.Options();
                opts.inSampleSize=4;
                Bitmap bitmap=BitmapFactory.decodeResource(getResources(), resID, opts);*/
                imageView.setImageBitmap(fixBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage){
        ByteArrayOutputStream bytes= new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        System.exit(0);
    }
}
