package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import models.User;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private static final String KEY_ADMISSIONNO = "admission_number";
    private static final String KEY_HOSTEL = "hostel";
    private static final String KEY_USERID = "user_dd";


    private Button logoutBtn, NotificationBtn, AttendanceBtn, FeesBtn, MessoutBtn, SickBtn, MenuBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    private String userName,hostel, admissionNumber, name;
    private TextView userNameView;
    private ImageView profilePicture;
    private Task<DocumentSnapshot> documentReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        hostel = intent.getStringExtra("hostel");
        admissionNumber = intent.getStringExtra("admission_number");
        initWidgets();
        getUserData(admissionNumber, hostel);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                intent.putExtra("hostel", hostel);
                intent.putExtra("admission_number", admissionNumber);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                finish();
                startActivity(new Intent(Home.this, Login.class));

            }
        });

        NotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Notification.class));
            }
        });

        AttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Attendance.class));
            }
        });
        FeesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Fees.class));
            }
        });
        MessoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Messout.class));
            }
        });
        SickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Sick.class));
            }
        });
        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Menu.class);
              //  intent.putExtra("userName",userName);
               // intent.putExtra("hostel",hostel);
                startActivity(intent);

            }
        });
    }
    private void getUserData(String admissionNumber, String hostel){
        documentReference = firebaseFirestore.collection("inmates").document(hostel).collection("users").document(admissionNumber).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                   User user = documentSnapshot.toObject(User.class);
                   name = user.getName();
                   setUserData(name);
                }
            }
        });
    }

    private void initWidgets(){
        profilePicture = findViewById(R.id.imageView2);
        userNameView = findViewById(R.id.textView5);
        logoutBtn = (Button) findViewById(R.id.button2);
        NotificationBtn = (Button) findViewById(R.id.button5);
        AttendanceBtn = (Button) findViewById(R.id.button6);
        FeesBtn = (Button) findViewById(R.id.button7);
        MessoutBtn = (Button) findViewById(R.id.button8);
        SickBtn = (Button) findViewById(R.id.button9);
        MenuBtn = (Button)findViewById(R.id.btnmenu);

    }

    private void setUserData(String name){
        userNameView.setText("Welcome " + name);
        StorageReference profileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile_picture/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });
    }

}