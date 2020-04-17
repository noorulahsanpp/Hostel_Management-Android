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
    private FirebaseAuth auth;
    private TextView emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        emailView = (TextView)findViewById(R.id.emailView);

//        FirebaseUser user = auth.getCurrentUser();

//        emailView.setText(user.getEmail());

        logoutBtn = (Button)findViewById(R.id.button2);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                finish();
                startActivity(new Intent(Home.this, Login.class));

            }
        });
    }
}
