package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final String KEY_ADMISSIONNO = "admission_number";
    private static final String KEY_HOSTEL = "hostel";
    private static final String KEY_USERID = "user_dd";
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedPreferences;
    private TextView signUpTv;
    private Button loginbtn;
    private EditText emailEt,passwordEt;
    private FirebaseAuth auth;
    private String userID,loginPhone, loginAdmission, loginHostel, loginName;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        progressdialog = new ProgressDialog(Login.this);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(Login.this, Home.class));
        }

        signUpTv = findViewById(R.id.signupTv);
        loginbtn = (Button) findViewById(R.id.button);
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
                getUserData(userID);
                setSharedPreferences();
                progressdialog.dismiss();
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressdialog.dismiss();
                Toast.makeText(getBaseContext(), "Invalid Email/Password. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getUserData(String userid)
    {
        getHostel(userid);
        firebaseFirestore.collection("inmates").document(loginHostel).collection("users").whereEqualTo("user_id", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        loginAdmission = document.get("admission_number").toString();
                        loginHostel = document.get("hostel").toString();
                        loginName = document.get("name").toString();
                        loginPhone = document.get("phone_number").toString();
                    }

                }
                else
                {
                    Log.d(TAG, "onFailure: Query Unsuccessful");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Query failed");
            }
        });
    }

    private void getHostel(String userid)
    {
        firebaseFirestore.collection("login").whereEqualTo("user_id", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        loginHostel = document.get("hostel").toString();
                    }
                }
                else
                {
                    Log.d(TAG, "onFailure: Query Unsuccessful");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Query failed");
            }
        });
    }

    public void setSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userID);
        editor.putString("hostel", loginHostel);
        editor.putString("admissionno", loginAdmission);
        editor.putString("phone", loginPhone);
        editor.putString("name", loginName);
        editor.commit();
    }





}
