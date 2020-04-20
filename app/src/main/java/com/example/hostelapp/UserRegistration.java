package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Utils.FirebaseMethods;

public class UserRegistration extends AppCompatActivity {
    private static final String TAG = "UserRegistration";

    private String admissionnumber, email, password, phone;
    private EditText mName, mPassword, mEmail, mPhone;
    private Context mContext;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private TextView loginBtn;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mContext = UserRegistration.this;
        firebaseMethods = new FirebaseMethods(mContext);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        if(auth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(UserRegistration.this, Home.class));
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        initWidgets();
        init();

    }

    private void init(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admissionnumber = mName.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                phone = mPhone.getText().toString().trim();
                if(checkInputs(email, password, phone, admissionnumber)) {
                    registration();
                }
            }
        });

    }

    private void initWidgets(){
        Log.d("TAG", "initWidgets: Initialising widgets");
        signUpBtn = findViewById(R.id.button4);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        loginBtn = findViewById(R.id.textView);
    }

    private boolean checkInputs(String email, String password, String phone, String admissionnumber)
    {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Input Email");
            mEmail.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(password)) {
            mPassword.setError("Input Password");
            mPassword.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(admissionnumber)) {
            mPassword.setError("Input Password");
            mPassword.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(phone)) {
            mPassword.setError("Input Password");
            mPassword.requestFocus();
            return false;
        }
        else if(password.length() < 6)
        {
            mPassword.setError("Invalid Password");
            mPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void registration() {

                progressDialog.setMessage("Registering...");
                progressDialog.show();
                try {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                    userID = auth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    //user.put("Name", name);
                                    user.put("Email", email);
                                    user.put("Phone", phone);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UserRegistration.this, "Created Successfully for : " + userID, Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Home.class));
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UserRegistration.this, "Error!", Toast.LENGTH_SHORT).show();
                                                    Log.d("TAG", e.toString());
                                                }
                                            });
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(UserRegistration.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        catch(Exception e)
                {
                    System.out.println("Error is :" + e);
                }
        }
}
