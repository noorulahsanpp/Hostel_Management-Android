package com.example.hostelapp;


//import org.threeten.bp.LocalDate;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Utils.FirebaseMethods;


import static android.provider.Settings.System.DATE_FORMAT;

public class Attendance extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{
    private static final String TAG = "Messout";
    public static final String MyPREFERENCES = "MyPrefs" ;
    static final long ONE_DAY = 24 * 60 * 60 * 1000L;
Button okBtn;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    ListView listView;
    private  Date d1, d2;

    SharedPreferences sharedPreferences;
    String monthIndex,selectedYear,absent;
    Spinner month, year;

    int count = 0,presentdays;
    private String hostel,admissionNumber;
    public static String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_attendance);
        //listView = findViewById(R.id.listview);
        getSharedPreference();
        mContext = Attendance.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        //calendarView = findViewById(R.id.calendar);
        okBtn = findViewById(R.id.ok);


        month = (Spinner) findViewById(R.id.month);
        year = (Spinner) findViewById(R.id.spinneryear);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(month);
            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        setAdapter();
       // tine();
    }


//        public void tine() {
//            final List<HashMap<String, String>> listitems = new ArrayList<>();
//            final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.attendance_listview, new String[]{"First Line"},new int[]{R.id.dates});
//            String fromDate = 1 + " " + 2 + " " + 2021;
//            String toDate = 28 + " " + 2 + " " + 2021;
//            Date startDate = new Date();
//            Date endDate = new Date();
//            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
//            try {
//                startDate = simpleDateFormat.parse(fromDate);
//                endDate = simpleDateFormat.parse(toDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Calendar start = Calendar.getInstance();
//            start.setTime(startDate);
//            Calendar end = Calendar.getInstance();
//            end.setTime(endDate);
//            Date date1 = start.getTime();
//            Date date2 = end.getTime();
//
//            // CollectionReference collectionReference = firebaseFirestore.collection("inmates").document("LH").collection("attendance");
//            // Query query = collectionReference.whereArrayContains("absents", "LH002").whereGreaterThanOrEqualTo("date", date1).whereLessThanOrEqualTo("date", date2);
//            CollectionReference collectionReference = firebaseFirestore.collection("inmates").document(hostel).collection("attendance");
//            Query query = collectionReference.whereArrayContains("absents", admissionNumber).whereGreaterThanOrEqualTo("date", date1).whereLessThanOrEqualTo("date", date2);
//            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String dates = document.getId();
//                            count = count+1;
//                            HashMap<String, String> resultMap = new HashMap<>();
//                            resultMap.put("FirstLine", dates);
//                            listitems.add(resultMap);
//                            listView.setAdapter(adapter);
//
//
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                    absnt.setText(count+"");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(TAG, "Error getting documents: ", e);
//                }
//            });
//
//
//
//        }



//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.MONTH, month);
//        setAdapter();
//        present = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        documentReference = firebaseFirestore.collection("inmates").document("LH").collection("users").document("LH002").collection("messout").document("june").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
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


    private void setAdapter()
    {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int j = 2018; j <= 2025; j++) {
            years.add(Integer.toString(j));
        }
        ArrayAdapter<String> yearadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        year.setAdapter(yearadapter);
        List<String> monthsList = new ArrayList<String>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            monthsList.add(months[i]);
        }
        ArrayAdapter<String> monthadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        month.setAdapter(monthadapter);
        month.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedYear = year.getSelectedItem().toString();
        String months1 = month.getSelectedItem().toString();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            if (months1 == months[i]) {
                monthIndex = String.valueOf(i+1);
                break;
            }}
        date = selectedYear + "-0" + monthIndex;
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        DocumentReference documentReference2 = firebaseFirestore.collection("inmates").document(hostel+"").collection("users").document(admissionNumber+"").collection("attendance").document(date+"");
//        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot documentSnapshot = task.getResult();
//                if (documentSnapshot.exists()){
//                    absent = documentSnapshot.get("daysabsent").toString();
//                    presentdays = days - Integer.parseInt(absent);
//                    prsnt.setText( String.valueOf(presentdays));
//                    absnt.setText(absent);
//                }
//               else{
//                    prsnt.setText("");
//                    absnt.setText("");
//                }
//            }
//        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DatesDisplay.class);
                i.putExtra("month",monthIndex+"");
                i.putExtra("year",selectedYear+"");
                startActivity(i);
            }
        });

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    public void getAttendace() {
////                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
////                    String s2 = "01 06 2020";
////                    String s1 = "30 06 2020";
////
////                    try {
////                        d1 = simpleDateFormat.parse(s1);
////                        d2 =  simpleDateFormat.parse(s2);
////                        Log.d(TAG, "/////////////////////////////////////////////////////getAttendace: "+new Timestamp(d1));
////                    }
////                    catch (Exception e){
////                        Log.d(TAG, "getAttendace: "+e);
////                    }
//        // CollectionReference collectionReference = firebaseFirestore.collection("inmates").document("LH").collection("attendance");
//        // Query query = collectionReference.whereArrayContains("absents", "LH001");
//        CollectionReference collectionReference = firebaseFirestore.collection("inmates").document(hostel).collection("attendance");
//        Query query = collectionReference.whereArrayContains("absents", admissionNumber);
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        count = count + 1;
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//                absnt.setText(count + "");
//            }
//        });
//    }


    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
    }


}
