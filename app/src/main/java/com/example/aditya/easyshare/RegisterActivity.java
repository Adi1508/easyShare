package com.example.aditya.easyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by aditya on 27/1/18.
 */


public class RegisterActivity extends AppCompatActivity{

    public FirebaseAuth firebaseAuth;
    public EditText name,email,password;
    private Button register, login, upload;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("LoginActivity Started");

        firebaseAuth = FirebaseAuth.getInstance();

        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        upload=findViewById(R.id.upload_login);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        password=findViewById(R.id.pass);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail=email.getText().toString().trim();
                String inputPass=password.getText().toString().trim();

                if(TextUtils.isEmpty(inputEmail)){
                    Toast.makeText(getApplicationContext(),"Enter Email!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(inputPass)){
                    Toast.makeText(getApplicationContext(),"Enter Password!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(inputPass.length()<6){
                    Toast.makeText(getApplicationContext(),"Password too short, enter minimum 6 characters!",Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPass)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this,"createUserWithEmail:onComplete:"+task.isSuccessful(), Toast.LENGTH_SHORT).show();
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
