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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Document;

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
    private String userID, username, room, block, admisson,batch,dept,hostel,regn;

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
                    if (document.exists())
                    {
                            username = document.get("name").toString();
                            block = document.get("block").toString();
                            dept = document.get("dept").toString();
                            room = document.get("room").toString();
                            hostel = document.get("hostel").toString();
                            batch = document.get("batch").toString();
                            String id = document.getId();
                            setName(username);
                            Map<String, Object> user = new HashMap<>();
                            user.put("regn","yes");
                            documentReference.set(user, SetOptions.merge());
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
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                    mAuth = FirebaseAuth.getInstance();
                                    userID = mAuth.getCurrentUser().getUid();

                                CollectionReference inmates = firebaseFirestore.collection("inmates");

                                if(hostel.equals("LH")) {
                                    DocumentReference LH = inmates.document("LH").collection("users").document(admisson);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Name", username);
                                    user.put("Admissionnumber", admisson);
                                    user.put("Userid", userID);
                                    user.put("Dept", dept);
                                    user.put("Batch", batch);

                                    LH.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            toastMessage("Success");
                                            progressDialog.dismiss();

                                                                                    }
                                    });
                                }
                                else {
                                    DocumentReference MH = inmates.document("MH").collection("users").document(admisson);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Name", username);
                                    user.put("Admissionnumber", admisson);
                                    user.put("Userid", userID);
                                    user.put("Dept", dept);
                                    user.put("Batch", batch);

                                    MH.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            toastMessage("Success");
                                            progressDialog.dismiss();
                                                                          }
                                    });

                                }

                                Intent intent = new Intent(UserRegistration.this, Home.class);
                                intent.putExtra("userName",username);
                                intent.putExtra("hostel",hostel);
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
}
