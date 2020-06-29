package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.utils.DateUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import Utils.FirebaseMethods;

public class Attendance extends AppCompatActivity {
    private static final String TAG = "Messout";
    static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    private MaterialCalendarView materialCalendarView;

    private  Date d1, d2;

    private int absent=0,present,year,month;
    private TextView prsnt,absnt;
    int count = 0;
    private List list;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_attendance);

        mContext = Attendance.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        prsnt = (TextView)findViewById(R.id.txtpresent);
        absnt=(TextView)findViewById(R.id.txtabsent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        present = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(false);
        }


        tine();






    }




        public void getAttendace(){

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

                public void tine() {
                    String fromDate = 20 + " " + 6 + " " + 2020;
                    String toDate = 22 + " " + 6 + " " + 2020;
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


                    while (date1.getTime()<= date2.getTime())
                    {

                        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
                        materialCalendarView.setDateSelected(date1,true);
                        date1 = new Date(date1.getTime()+(1000 * 60 * 60 * 24));
                           absent = absent+1;
                       }


                       absnt.setText(" "+absent);
                        int p = present - absent;
                        prsnt.setText(" "+p);





                    CollectionReference collectionReference = firebaseFirestore.collection("inmates").document("LH").collection("attendance");
                    Query query = collectionReference.whereArrayContains("absents", "LH002").whereGreaterThanOrEqualTo("date", date1).whereLessThanOrEqualTo("date", date2);
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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting documents: ", e);
                        }
                    });
                }


}







