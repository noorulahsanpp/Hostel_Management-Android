package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class forgotpassword extends AppCompatActivity {
    private EditText emailEt;
    private Button send;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        emailEt=findViewById(R.id.emailEt);
        send=findViewById(R.id.sendpswd);

        firebaseFirestore = FirebaseFirestore.getInstance();
       mAuth = FirebaseAuth.getInstance();

       send.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mAuth.sendPasswordResetEmail(emailEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       progressdialog.show();
                       if(task.isSuccessful())
                       {
                           Toast.makeText(getApplicationContext(), "Password send to your email", Toast.LENGTH_LONG).show();
                       }
                       else{
                           Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                       }
                   }
               });
           }
       });
    }
}