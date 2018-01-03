/*
package com.example.aditya.easyshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

*/
/**
 * Created by aditya on 2/1/18.
 *//*


public class LoginActivity extends AppCompatActivity {

    public static final int RequestSignInCode = 7;

    public FirebaseAuth firebaseAuth;

    public GoogleApiClient googleApiClient;

    com.google.android.gms.common.SignInButton signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.println("LoginActivity Started");

        signInButton = (SignInButton) findViewById(R.id.signIn);

        firebaseAuth = FirebaseAuth.getInstance();


        SharedPreferences loginPreferences = getApplicationContext().getSharedPreferences("accesskey", 0);
        if (loginPreferences.getInt("access", 0) == 0) {
            SharedPreferences.Editor editor = loginPreferences.edit();
            editor.putInt("access",0);
            editor.commit();
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignInMethod();
            }
        });
    }

    public void userSignInMethod() {
        Intent authIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(authIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignInCode) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        Toast.makeText(LoginActivity.this, "" + authCredential.getProvider(), Toast.LENGTH_LONG).show();
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            System.out.println("USER: " + firebaseUser.getDisplayName());
                            System.out.println("EMAIL: " + firebaseUser.getEmail());

                            SharedPreferences sp = getApplicationContext().getSharedPreferences("accesskey", 0);
                            SharedPreferences.Editor sedt = sp.edit();
                            sedt.putInt("access", 1);
                            sedt.commit();

                        } else {
                            Toast.makeText(LoginActivity.this, "something's wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
*/
