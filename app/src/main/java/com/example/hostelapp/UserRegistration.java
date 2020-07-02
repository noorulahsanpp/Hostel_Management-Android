package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import Utils.FirebaseMethods;
import models.User;

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
    private FirebaseMethods firebaseMethods;
    private String userID, username, room, block, admisson,batch,dept,hostel,regn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_registration);

        mContext = UserRegistration.this;
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseMethods = new FirebaseMethods(mContext);

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
       try {
           documentReference = firebaseFirestore.collection("registered").document(admissionnumber);
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
                           String id = document.getId();
                           setName(username);
                           Map<String, Object> user = new HashMap<>();
                           user.put("app_registration", "yes");
                           documentReference.set(user, SetOptions.merge());
                       } else {
                           Toast.makeText(getApplicationContext(), "Invalid registration number", Toast.LENGTH_LONG).show();
                       }
                   } else {
                       Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
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
                userlogin.put("admission_number", admissionnumber);
                userlogin.put("hostel", hostel);
                login.set(userlogin);
            }
            catch (Exception e){
                Log.d(TAG, "setLoginDetails: "+e);
            }
        }

    private void registration() {
        showProgress();
                try {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                    mAuth = FirebaseAuth.getInstance();
                                    userID = mAuth.getCurrentUser().getUid();

                                CollectionReference inmates = firebaseFirestore.collection("inmates");

                                if(hostel.equals("LH")) {
                                    documentReference = inmates.document("LH").collection("users").document(admisson);
                                }
                                else {
                                    documentReference = inmates.document("MH").collection("users").document(admisson);
                                }
                                User user = new User(userID, phone, mAuth.getCurrentUser().getEmail(), admisson, room, block, hostel, batch, dept, username);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            toastMessage("Success");
                                            setLoginDetails(userID, admisson, hostel);
                                            progressDialog.dismiss();
                                                                          }
                                    });
                                mAuth.signOut();
                                Intent intent = new Intent(UserRegistration.this, Login.class);
                                startActivity(intent);
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
}
