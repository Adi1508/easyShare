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

    Button getImage, uploadImage, resetContent, imageProcessing;
    ImageView showImage;
    EditText getImageName;
    Bitmap fixBitmap;

    ProgressDialog progressDialog;
    String getImageFromEditText = null;
    Uri uri;

    private StorageReference mStorageRef;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    public static String databasePath = "uploaded_images_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        firebaseAuth = FirebaseAuth.getInstance();

        getImage = (Button) findViewById(R.id.display);
        uploadImage = (Button) findViewById(R.id.upload);
        showImage = (ImageView) findViewById(R.id.imageView);
        getImageName = (EditText) findViewById(R.id.editText1);
        resetContent = (Button) findViewById(R.id.reset);
        imageProcessing = (Button) findViewById(R.id.imageProcessing);

        progressDialog = new ProgressDialog(MainActivity.this);

        imageProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageProcessing.class);
                startActivity(intent);
            }
        });
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        uploadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getImageFromEditText = getImageName.getText().toString();
                System.out.println("getImageFromEditText: " + getImageFromEditText);

                if (getImageFromEditText.matches("")) {
                    Toast.makeText(MainActivity.this, "Complete the name", Toast.LENGTH_LONG).show();
                } else if (showImage.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "select the image to upload", Toast.LENGTH_LONG).show();
                } else {
                    uploadImageToServer();
                }
            }
        });

        resetContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage.setImageResource(android.R.color.transparent);
                getImageName.getText().clear();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.displayStorage:
                Toast.makeText(this, "display storage is selected", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, DisplayStorage.class);
                startActivity(intent);

                return (true);
            case R.id.signOut:
                UserSignOutFunction();
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    public void UserSignOutFunction() {

        // Sing Out the User.
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {
                        Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                System.out.println(uri);
                fixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                showImage.setImageBitmap(fixBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImageToServer() {

        // Setting progressDialog Title.
        progressDialog.setTitle("Image is Uploading...");

        // Showing progressDialog.
        progressDialog.show();

        //StorageReference imageReference = mStorageRef.child(getImageFromEditText);
        StorageReference imageRef = mStorageRef.child(getImageFromEditText);

        showImage.setDrawingCacheEnabled(true);
        showImage.buildDrawingCache();

        Bitmap bitmap = showImage.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageData = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                // Hiding the progressDialog.
                progressDialog.dismiss();

                // Showing exception erro message.
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println(downloadUrl);

                // Hiding the progressDialog after done uploading.
                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, "The image is Uploaded", Toast.LENGTH_LONG).show();

                ImageUploadInfo imageUploadInfo = new ImageUploadInfo(getImageFromEditText, taskSnapshot.getDownloadUrl().toString());

                //in place of id use the name of person such that same person has all the images in a single node
                String ImageUploadId = databaseReference.push().getKey();

                //update the database with the uplaoded images information
                databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                // Setting progressDialog Title.
                progressDialog.setTitle("Image is Uploading...");

            }

        });
    }
}
