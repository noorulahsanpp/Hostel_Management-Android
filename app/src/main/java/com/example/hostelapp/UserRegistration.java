package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserRegistration extends AppCompatActivity {

    private EditText mName,mPassword,mEmail,mPhone;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        signUpBtn = findViewById(R.id.button4);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        loginBtn = findViewById(R.id.textView);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(UserRegistration.this, Home.class));
        }

        progressDialog = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

    }

    private void registration()
    {
        try {
            String name = mName.getText().toString().trim();
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            int phone = Integer.parseInt(mPhone.getText().toString().trim());

            progressDialog.setMessage("Registering...");
            progressDialog.show();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Input Email");
                mEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Input Password");
                mPassword.requestFocus();
                return;
            }


            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else {
                        Toast.makeText(UserRegistration.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("Error is :"+e);
            System.out.println("Error is :"+e);
        }
    }
}
