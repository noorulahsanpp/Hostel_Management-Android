package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import Utils.FirebaseMethods;

public class Attendance extends AppCompatActivity {
    private static final String TAG = "Messout";
    static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Task<DocumentSnapshot> documentReference;
    private FirebaseMethods firebaseMethods;
    private CalendarView calendarView;
    private  Date d1, d2;

    private int absent=0,present,year,month;
    private TextView prsnt,absnt;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_attendance);

        mContext = Attendance.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        calendarView = (CalendarView)findViewById(R.id.calendar);
        prsnt = (TextView)findViewById(R.id.txtpresent);
        absnt=(TextView)findViewById(R.id.txtabsent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        present = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

//        documentReference = firebaseFirestore.collection("inmates").document("LH").collection("users").document("LH002").collection("messout").document("june").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//
//                        DocumentSnapshot document = task.getResult();
//
//                        Date from = document.getDate("from");
//                        Date to = document.getDate("to");
//
//                        Calendar cal1 = Calendar.getInstance();
//                        cal1.setTime(from);
//
//
//                        Calendar cal2 = Calendar.getInstance();
//                        cal2.setTime(to);
//
//
//                        while(!cal1.after(cal2))
//                        {
//                            //  int solidColor = calendarView.getSolidColor(R.id.tabs);
//                            cal1.add(Calendar.DATE,1);
//                            absent = absent+1;
//                        }
//                        absnt.setText(" "+absent);
//                        int p = present - absent;
//                        prsnt.setText(" "+p);
//                 }
//
//
//                }
//
//                    });

        getAttendace();

    }
                public void getAttendace(){
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
//                    String s2 = "01 06 2020";
//                    String s1 = "30 06 2020";
//
//                    try {
//                        d1 = simpleDateFormat.parse(s1);
//                        d2 =  simpleDateFormat.parse(s2);
//                        Log.d(TAG, "/////////////////////////////////////////////////////getAttendace: "+new Timestamp(d1));
//                    }
//                    catch (Exception e){
//                        Log.d(TAG, "getAttendace: "+e);
//                    }
                    CollectionReference collectionReference = firebaseFirestore.collection("inmates").document("LH").collection("attendance");
                    Query query = collectionReference.whereArrayContains("absents", "LH001");
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    count = count+1;
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                            absnt.setText(count+"");
                        }
                    });
                }

                public void tine() throws ParseException {
                    String string = "June 1, 2020";
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = format.parse(string);
                    System.out.println(date);
                }
}







