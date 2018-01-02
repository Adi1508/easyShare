package com.example.aditya.easyshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by aditya on 2/1/18.
 */

public class LoginActivity extends AppCompatActivity {

    public static final int RequestSignInCode = 7;

    public FirebaseAuth firebaseAuth;

    public GoogleApiClient googleApiClient;

    com.google.android.gms.common.SignInButton signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (SignInButton) findViewById(R.id.signIn);

        firebaseAuth = FirebaseAuth.getInstance();



    }
}
