package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Utils.FirebaseMethods;

public class Complaints extends AppCompatActivity {
EditText ETtopic, ETdescription;
String topic,description;
    public static final String MyPREFERENCES = "MyPrefs" ;
Button submit;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    private String hostel,admissionNumber,name,roomno,block;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        ETtopic = findViewById(R.id.topic);
        ETdescription = findViewById(R.id.description);
        submit = findViewById(R.id.submit);
        getSharedPreference();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 topic = ETtopic.getText().toString();
                 description = ETdescription.getText().toString();
                if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(description) ){
                    Toast.makeText(Complaints.this, "Please type your complaints", Toast.LENGTH_SHORT).show();
                } else {
                    setcomplaints();
                    Toast.makeText(getApplicationContext(), "Your Complaint Registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Complaints.this, Home.class));
                }
            }
        });

    }

    public void setcomplaints(){
        Date date = setDate();
        CollectionReference collectionReference = firebaseFirestore.collection("inmates").document(hostel+"").collection("complaints");
        Map<String, Object> complaints = new HashMap<>();
        complaints.put("admission no",admissionNumber);
        complaints.put("topic",topic);
        complaints.put("name",name);
        complaints.put("room",roomno);
        complaints.put("block",block);
        complaints.put("description",description);
        collectionReference.document(date+"").set(complaints);
    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
        name = sharedPreferences.getString("name", "");
        roomno = sharedPreferences.getString("room", "");
        block = sharedPreferences.getString("block", "");
    }
    public Date setDate(){
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        Date today = start.getTime();
        return today;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
