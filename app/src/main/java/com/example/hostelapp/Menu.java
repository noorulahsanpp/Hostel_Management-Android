package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Menu extends AppCompatActivity {


          TextView breakfast;
          TextView lunch;
          TextView snacks;
          TextView dinner;

    private Date dateObj1;
    private String currentdate;
          FirebaseFirestore firebaseFirestore;
          DocumentReference db;
          FirebaseAuth mAuth;
          String hostel,username,userID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
            getSupportActionBar().hide();
            setContentView(R.layout.activity_menu);

               breakfast = (TextView)findViewById(R.id.txtbreakfast);
               lunch = (TextView)findViewById(R.id.txtlunch);
                snacks = (TextView)findViewById(R.id.txtevening);
                 dinner = (TextView)findViewById(R.id.txtdinner);




            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateandTime = simpleDateFormat.format(new Date());




                firebaseFirestore = FirebaseFirestore.getInstance();
      firebaseFirestore.collection("inmates").document("LH").collection("foodmenu").whereEqualTo("date",currentDateandTime).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if (task.isSuccessful())
              {
                  for(QueryDocumentSnapshot document : task.getResult()){

                      String brkfst = document.get("breakfast").toString();
                      String dinnr = document.get("dinner").toString();
                      String eveng = document.get("evening").toString();
                      String lnch = document.get("lunch").toString();

                    breakfast.setText(brkfst);
                    dinner.setText(dinnr);
                    snacks.setText(eveng);
                    lunch.setText(lnch);

                  }
              }
          }
      });


        }
    }




