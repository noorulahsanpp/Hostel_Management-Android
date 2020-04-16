package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextView signUpTv;
    private Button loginbtn;
    private EditText emailEt,passwordEt;
    private FirebaseAuth auth;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressdialog = new ProgressDialog(Login.this);
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(Login.this, Home.class));
        }

        signUpTv = findViewById(R.id.signupTv);
        loginbtn = (Button)findViewById(R.id.button);
        emailEt = (EditText)findViewById(R.id.editText1);
        passwordEt = (EditText)findViewById(R.id.editText2);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
            }
        });
    }

    private void loginUser()
    {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (TextUtils.isEmpty(email))
        {
            emailEt.setError("Input Email");
            emailEt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            passwordEt.setError("Input Password");
            passwordEt.requestFocus();
            return;
        }

        progressdialog.setMessage("Logging in...");
        progressdialog.show();

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressdialog.dismiss();
                startActivity(new Intent(Login.this, Home.class));
                finish();
            }
        });
    }
}
