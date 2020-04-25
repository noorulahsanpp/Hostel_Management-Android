package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";

    private TextView signUpTv;
    private Button loginbtn;
    private EditText emailEt,passwordEt;
    private FirebaseAuth auth;
    private String userID,admissionNumber;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressdialog = new ProgressDialog(Login.this);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
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
                startActivity(new Intent(getApplicationContext(), Verification.class));
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
                userID = auth.getCurrentUser().getUid();
                getAdmissionNo(userID);
                progressdialog.dismiss();
                startActivity(new Intent(Login.this, Home.class));
                finish();
            }
        });
    }
    private void getAdmissionNo(String userid)
    {
        firebaseFirestore.collection("users").whereEqualTo("Userid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        admissionNumber = document.get("Userid").toString();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Query failed");
            }
        });
    }
}
