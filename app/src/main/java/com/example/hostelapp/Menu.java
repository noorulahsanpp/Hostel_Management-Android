package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu extends AppCompatActivity {

         Button back;
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
                 back = (Button)findViewById(R.id.btnback);

            Intent intent = getIntent();
            username = intent.getStringExtra("userName");
            hostel = intent.getStringExtra("hostel");




        DocumentReference db = firebaseFirestore.collection("inmates").document(hostel).collection("menu").document();
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


              back.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              startActivity(new Intent(Menu.this, Home.class));

                                                             }
               });
        }
    }




