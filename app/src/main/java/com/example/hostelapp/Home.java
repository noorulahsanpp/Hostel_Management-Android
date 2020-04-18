package com.example.hostelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Home extends AppCompatActivity {

    private Button logoutBtn, NotificationBtn, AttendanceBtn, FeesBtn, MessoutBtn, SickBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    private String userName;
    private TextView userNameView;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();

        profilePicture = findViewById(R.id.imageView2);
        userNameView = findViewById(R.id.textView5);
        logoutBtn = (Button)findViewById(R.id.button2);
        NotificationBtn = (Button)findViewById(R.id.button5);
        AttendanceBtn = (Button)findViewById(R.id.button6);
        FeesBtn = (Button)findViewById(R.id.button7);
        MessoutBtn = (Button)findViewById(R.id.button8);
        SickBtn = (Button)findViewById(R.id.button9);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                userNameView.setText("Welcome "+documentSnapshot.getString("Name"));
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Profile.class));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                finish();
                startActivity(new Intent(Home.this, Login.class));

            }
        });

        NotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Notification.class));
            }
        });

        AttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Attendance.class));
            }
        });
        FeesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Fees.class));
            }
        });
       MessoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Messout.class));
            }
        });
        SickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Sick.class));
            }
        });


    }
}
