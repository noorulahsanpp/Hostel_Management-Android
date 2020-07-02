package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Utils.FirebaseMethods;

public class Fees extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    Spinner month, year;
    TextView mess, rent, extras, common, due, total, gtotal;
    String monthIndex,selectedYear;

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    SharedPreferences sharedPreferences;
    private String hostel, admissionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fees);

        mContext = Fees.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);

        initWidgets();
        setAdapter();
        getSharedPreference();


    }

    private void initWidgets() {

        month = (Spinner) findViewById(R.id.month);
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
        year = (Spinner) findViewById(R.id.spinneryear);
        rent = (TextView) findViewById(R.id.rent);
        extras = (TextView) findViewById(R.id.extras);
        common = (TextView) findViewById(R.id.common);
        due = (TextView) findViewById(R.id.txtdue);
        total = (TextView) findViewById(R.id.txttotal);
        gtotal = (TextView) findViewById(R.id.txtgrand);
        mess = (TextView) findViewById(R.id.mess);

    }
    private void setAdapter()
    {

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int j = 2018; j <= thisYear; j++) {

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

        String date = selectedYear + "-0" + monthIndex;
        System.out.println(date);
        DocumentReference documentReference= firebaseFirestore.collection("inmates").document(hostel).collection("fee").document(date);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String grandtotal = document.get("veg").toString();
                    gtotal.setText(grandtotal);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Doesn't Exist", Toast.LENGTH_LONG).show();
                    gtotal.setText("");

                }
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









