package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Menu extends AppCompatActivity {
    private static final String TAG = "Menu";
    Button btnback;
    TextView breakfast;
    TextView lunch;
    TextView snacks;
    TextView dinner;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String hostel,username,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        breakfast = (TextView)findViewById(R.id.txtbreakfast);
        lunch = (TextView)findViewById(R.id.txtlunch);
        snacks = (TextView)findViewById(R.id.txtevening);
        dinner = (TextView)findViewById(R.id.txtdinner);
        Intent intent = getIntent();
        username = intent.getStringExtra("userName");
        hostel = intent.getStringExtra("hostel");

        btnback = (Button)findViewById(R.id.btnback);

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference db = firebaseFirestore.collection("inmates").document(hostel).collection("menu").document(userID);
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String brkfst= document.get("Breakfast").toString();
                String lnch= document.get("Lunch").toString();
                String snck= document.get("Snacks").toString();
                String dnnr= document.get("Dinner").toString();
                breakfast.setText(brkfst);
                lunch.setText(lnch);
                snacks.setText(snck);
                dinner.setText(dnnr);



            }
        });


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Menu.this, Home.class));
            }
        });
    }
}
