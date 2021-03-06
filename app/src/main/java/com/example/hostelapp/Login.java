package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final String KEY_ADMISSIONNO = "admission_number";
    private static final String KEY_HOSTEL = "hostel";
    private static final String KEY_USERID = "user_dd";
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedPreferences;
    private TextView signUpTv,forgot;
    private Button loginbtn;
    private EditText emailEt,passwordEt;
    private FirebaseAuth auth;
    private String userID,loginPhone, loginAdmission, loginHostel, loginName, loginEmail,loginBlock,loginRoom;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressdialog;
    private FirebaseMessaging firebaseMessaging;


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


        initWidgets();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forgotpassword.class));
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Verification.class));
            }
        });
    }
    public void initWidgets(){
        signUpTv = findViewById(R.id.signupTv);
        loginbtn = (Button) findViewById(R.id.button);
        emailEt = (EditText)findViewById(R.id.editText1);
        passwordEt = (EditText)findViewById(R.id.editText2);
        forgot = findViewById(R.id.forgot);
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
                FirebaseUser user =auth.getCurrentUser();
                try {
                    if (user.isEmailVerified()){
                        Log.d(TAG, "onSuccess: Email Verified");
                        getHostel(userID);
                        progressdialog.dismiss();
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                    }
                    else {
                        progressdialog.dismiss();
                        Toast.makeText(getBaseContext(), "Email not verified \n Check your inbox.", Toast.LENGTH_LONG).show();
                    }
                }
                catch (NullPointerException e){
                    Log.e(TAG, "onSuccess: NullPointerException "+e.getMessage() );
                }
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
        firebaseFirestore.collection("inmates").document(loginHostel+"").collection("users").whereEqualTo("user_id", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        loginAdmission = document.get("admission_no").toString();
                        loginHostel = document.get("hostel").toString();
                        loginName = document.get("name").toString();
                        loginPhone = document.get("phone").toString();
                        loginEmail = document.get("email").toString();
                        loginRoom = document.get("room").toString();
                        loginBlock = document.get("block").toString();

                    }
                    setSharedPreferences();
                    cloudMessage();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Query failed"+e);
            }
        });
    }

    private void getHostel(final String userid)
    {
        firebaseFirestore.collection("login").document(userid+"").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        loginHostel = document.get("hostel").toString();
                        getUserData(userid);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
            }
        });
    }

    public void setSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userID);
        editor.putString("hostel", loginHostel);
        editor.putString("admissionno", loginAdmission);
        editor.putString("block", loginBlock);
        editor.putString("room", loginRoom);
        editor.putString("phone", loginPhone);
        editor.putString("name", loginName);
        editor.putString("email", loginEmail);
        editor.commit();
    }

    public void cloudMessage(){
        firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic(loginHostel+"")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to "+loginHostel;
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                    }
                });
    }
    @Override
    public void onBackPressed() {

            moveTaskToBack(true); // exist app
            finish();
    }
}