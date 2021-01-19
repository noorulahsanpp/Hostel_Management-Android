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
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
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
        getSharedPreference();
        mContext = Attendance.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        okBtn = findViewById(R.id.ok);

        month = (Spinner) findViewById(R.id.month);
        year = (Spinner) findViewById(R.id.spinneryear);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(month);
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
        setAdapter();
    }

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

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
    }
}
