package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private Button logoutBtn;
    private Button NotificationBtn;
    private Button AttendanceBtn;
    private Button FeesBtn;
    private Button MessoutBtn;
    private Button SickBtn;
    private FirebaseAuth auth;
//    private TextView emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

//        emailView = (TextView)findViewById(R.id.emailView);

//        FirebaseUser user = auth.getCurrentUser();

//        emailView.setText(user.getEmail());

        logoutBtn = (Button)findViewById(R.id.button2);
        NotificationBtn = (Button)findViewById(R.id.button5);
        AttendanceBtn = (Button)findViewById(R.id.button6);
        FeesBtn = (Button)findViewById(R.id.button7);
        MessoutBtn = (Button)findViewById(R.id.button8);
        SickBtn = (Button)findViewById(R.id.button9);

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
