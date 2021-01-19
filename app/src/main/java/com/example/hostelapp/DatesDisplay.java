package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Utils.FirebaseMethods;

public class DatesDisplay extends AppCompatActivity {
    private static final String TAG = "Messout";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    ListView listView;
    private  Date d1, d2;

    SharedPreferences sharedPreferences;
    String monthIndex,selectedYear,absent;
    String month, year;
    private TextView prsnt,absnt;
    int count = 0,presentdays;
    private String hostel,admissionNumber;
    public static String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendance");
        listView = findViewById(R.id.listview);
        prsnt = findViewById(R.id.txtpresent);
        absnt=findViewById(R.id.txtabsent);
        mContext = DatesDisplay.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        getSharedPreference();
        Intent intent = getIntent();
       month = intent.getStringExtra("month");
       year = intent.getStringExtra("year");
       tine();
    }

    public void tine() {
        final List<HashMap<String, Object>> listitems = new ArrayList<>();
      ArrayList<String> dates = new ArrayList<>();

        String fromDate = 1 + " " + month + " " + year;
        String toDate = 31 + " " + month + " " + year;
        Date startDate = new Date();
        Date endDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        try {
            startDate = simpleDateFormat.parse(fromDate);
            endDate = simpleDateFormat.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        Date date1 = start.getTime();
        Date date2 = end.getTime();
        Calendar cal = Calendar.getInstance();
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // CollectionReference collectionReference = firebaseFirestore.collection("inmates").document("LH").collection("attendance");
        // Query query = collectionReference.whereArrayContains("absents", "LH002").whereGreaterThanOrEqualTo("date", date1).whereLessThanOrEqualTo("date", date2);
        CollectionReference collectionReference = firebaseFirestore.collection("inmates").document(hostel+"").collection("attendance");
        Query query = collectionReference.whereArrayContains("absents", admissionNumber+"").whereGreaterThanOrEqualTo("date", date1).whereLessThanOrEqualTo("date", date2);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String date = document.getId();
                        count = count + 1;
                        dates.add(date);
                    }
                    absnt.setText(count+"");
                    presentdays = days -count;
                    prsnt.setText( String.valueOf(presentdays));
                        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                        for (int i = 0; i < dates.size(); i++) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Firstline", dates.get(i));
                            list.add(map);
                        }
                        String[] from = {"Firstline"};
                        int to[] = {R.id.dates};
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), list, R.layout.attendance_listview, from, to);
                        listView.setAdapter(simpleAdapter);


                } else {
                    Toast.makeText(getApplicationContext(), "Attendance Not Published Yet", Toast.LENGTH_LONG).show();
                    finish();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: ", e);
                Toast.makeText(getApplicationContext(), "Attendance Not Published Yet", Toast.LENGTH_LONG).show();
                finish();
            }
        });



    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
    }
}