package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.FillEventHistory;
import android.util.EventLog;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;


import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Path;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DayOfWeek;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Utils.FirebaseMethods;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;

public class Attendance extends AppCompatActivity {
    static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Task<DocumentSnapshot> documentReference;
    private FirebaseMethods firebaseMethods;
    private CalendarView calendarView;

    private int absent=0,present,year,month;
    private TextView prsnt,absnt;


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

        documentReference = firebaseFirestore.collection("inmates").document("LH").collection("users").document("LH002").collection("messout").document("june").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();

                        Date from = document.getDate("from");
                        Date to = document.getDate("to");

                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(from);


                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(to);


                        while(!cal1.after(cal2))
                        {
                            //  int solidColor = calendarView.getSolidColor(R.id.tabs);
                            cal1.add(Calendar.DATE,1);
                            absent = absent+1;
                        }
                        absnt.setText(" "+absent);
                        int p = present - absent;
                        prsnt.setText(" "+p);
                 }


                }

                    });
                }
}







