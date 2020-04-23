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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class UserRegistration extends AppCompatActivity {
    private static final String TAG = "UserRegistration";

    private String admissionnumber, email, password, phone;
    private EditText mAdmissionumber, mPassword, mEmail, mPhone;
    private Context mContext;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private ProgressDialog progressDialog;
    private TextView loginBtn;
    private FirebaseFirestore firebaseFirestore;
    private String userID, username, room, block, admisson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mContext = UserRegistration.this;
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        admisson = intent.getStringExtra("adnumber");
        initWidgets();
        init();
        getData(admisson);
    }

    private void init(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admissionnumber = mAdmissionumber.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                phone = mPhone.getText().toString().trim();
                if(checkInputs(email, password, phone, admissionnumber)) {
//                    firebaseMethods.registerNewUser("", password, email);
                    registration();
                }
            }
        });
    }

    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initialising widgets");
        signUpBtn = findViewById(R.id.button4);
        mAdmissionumber = findViewById(R.id.name);
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
            mAdmissionumber.setError("Cant be empty");
            mAdmissionumber.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(phone)) {
            mPhone.setError("Input Phone");
            mPhone.requestFocus();
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
    public void getData(String admissionnumber)
    {
        documentReference = firebaseFirestore.collection("registered").document(admissionnumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                            username = document.get("name").toString();
//                            String block = document.get("block").toString();
//                            String dept = document.get("dept").toString();
//                            String room = document.get("room").toString();
//                            String sem = document.get("sem").toString();
//                            String id = document.getId();
                            setName(username);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Invalid registration number", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registration() {

                progressDialog.setMessage("Registering...");
                progressDialog.show();
                try {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                    mAuth = FirebaseAuth.getInstance();
                                    userID = mAuth.getCurrentUser().getUid();
//
                                WriteBatch batch = firebaseFirestore.batch();
                                DocumentReference inmates = firebaseFirestore.collection("inmates").document("MH").collection("users").document(admisson);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Name", username);
                                inmates.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        toastMessage("Success");
                                    }
                                });

                                    //
//                                    DocumentReference documentReference = firebaseFirestore.collection("inmates").document(userID);
//                                    Map<String, Object> user = new HashMap<>();
//                                    //user.put("Name", name);
//                                    user.put("Email", email);
//                                    user.put("Phone", phone);
//                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(UserRegistration.this, "Created Successfully for : " + userID, Toast.LENGTH_SHORT).show();
//                                            finish();
//                                            startActivity(new Intent(getApplicationContext(), Home.class));
//                                        }
//                                    })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    progressDialog.dismiss();
//                                                    Toast.makeText(UserRegistration.this, "Error!", Toast.LENGTH_SHORT).show();
//                                                    Log.d("TAG", e.toString());
//                                                }
//                                            });
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



    /**
     * Customisable toast
     * @param message
     */

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }

    private void setName(String name){
        mAdmissionumber.setText(name.toString());
    }
}
