package com.example.hostelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

import models.User;

public class Menu extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = "Menu";
    TextView breakfast;
          TextView lunch;
          TextView snacks;
          TextView dinner;

    private Date dateObj1;
    private String currentdate;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference db;
    private FirebaseAuth mAuth;
    private String hostel = "";
    private String username = "";
    private String userID = "";
    private String brkfst = "";
    private String dinnr = "";
    private String eveng = "";
    private String lnch = "";
    private Date today;
    SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
            getSupportActionBar().hide();
            setContentView(R.layout.activity_menu);

            sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            hostel = sharedPreferences.getString("hostel", "");

            initWidgets();
            firebaseFirestore = FirebaseFirestore.getInstance();
            getData();
        }
        public void initWidgets(){
            breakfast = (TextView)findViewById(R.id.txtbreakfast);
            lunch = (TextView)findViewById(R.id.txtlunch);
            snacks = (TextView)findViewById(R.id.txtevening);
            dinner = (TextView)findViewById(R.id.txtdinner);
        }

        public void setData(){
            breakfast.setText(brkfst);
            dinner.setText(dinnr);
            snacks.setText(eveng);
            lunch.setText(lnch);
        }

        public void getData(){
            today = setDate();
            firebaseFirestore.collection("inmates").document(hostel).collection("foodmenu").whereEqualTo("date",today).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        brkfst = document.get("breakfast").toString();
                        dinnr = document.get("dinner").toString();
                        eveng = document.get("eve").toString();
                        lnch = document.get("lunch").toString();
                    }
                    setData();
                }
            });
        setData();
        }

    public Date setDate(){
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Date today = start.getTime();
        return today;
    }
    }




