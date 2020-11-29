package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.HashMap;
import java.util.Map;


public class UserRegistration extends AppCompatActivity {
    private static final String TAG = "UserRegistration";

    private String email, password, phone;
    private EditText mAdmissionumber, mPassword, mEmail, mPhone;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView loginBtn;
    private FirebaseFirestore firebaseFirestore;

    private String userID, username, room, block, admisson,batch,dept,hostel;
    String localHostel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_registration);

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
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                phone = mPhone.getText().toString().trim();
                if(checkInputs(email, password, phone)) {
                    registration();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Verification.class);
                startActivity(i);
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

    private boolean checkInputs(String email, String password, String phone)
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
       try {
           DocumentReference documentReference = firebaseFirestore.collection("registered").document(admissionnumber);
           documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       if (document.exists()) {
                           username = document.get("name").toString();
                           block = document.get("block").toString();
                           dept = document.get("department").toString();
                           room = document.get("room").toString();
                           hostel = document.get("hostel").toString();
                           batch = document.get("semester").toString();
                           setName(username);

                       } else {
                           Toast.makeText(getApplicationContext(), "Invalid registration number", Toast.LENGTH_LONG).show();
                       }
                   } else {
                       Toast.makeText(getApplicationContext(), "Failed. Please try again.", Toast.LENGTH_LONG).show();
                   }
               }

           });
       }
       catch (Exception e){
           Log.d(TAG, "getData: "+e);
       }
        }

        public void setLoginDetails(String userid, String admissionnumber, String hostel)
        {
            try {
                DocumentReference login = firebaseFirestore.collection("login").document(userid);
                Map<String, Object> userlogin = new HashMap<>();
                userlogin.put("user_id", userid);
                userlogin.put("admission_no", admissionnumber);
                userlogin.put("hostel", hostel);
                login.set(userlogin);
            }
            catch (Exception e){
                Log.d(TAG, "setLoginDetails: "+e);
            }
        }

    private void registration() {
        showProgress();
        final CollectionReference inmates = firebaseFirestore.collection("inmates");
                try {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth = FirebaseAuth.getInstance();
                                userID = mAuth.getCurrentUser().getUid();
                                emailVerification();

                                Map<String, Object> detail = new HashMap<>();
                                detail.put("user_id", userID);
                                detail.put("name", username);
                                detail.put("phone", phone);
                                detail.put("email", email);
                                detail.put("admission_no", admisson);
                                detail.put("room", room);
                                detail.put("block", block);
                                detail.put("hostel", hostel);
                                detail.put("semester", batch);
                                detail.put("department", dept);

//                                User user = new User(userID, phone, mAuth.getCurrentUser().getEmail(), admisson, room, block, hostel, batch, dept, username);

                                firebaseFirestore.collection("inmates").document(hostel).collection("users").document(admisson).set(detail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            toastMessage("Success");
                                            setLoginDetails(userID, admisson, hostel);
                                            progressDialog.dismiss();
                                            regUserStatus();
                                            mAuth.signOut();
                                            Intent intent = new Intent(UserRegistration.this, Login.class);
                                            startActivity(intent);
                                                                          }
                                    }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Failed"+e);
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

    private void showProgress()
    {
        progressDialog.setMessage("Registering...");
        progressDialog.show();
    }

    public void regUserStatus(){
        DocumentReference documentReference = firebaseFirestore.collection("registered").document(admisson);
        Map<String, Object> us = new HashMap<>();
        us.put("app_reg", "yes");
        documentReference.set(us, SetOptions.merge());
    }

    public void emailVerification(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }
}
