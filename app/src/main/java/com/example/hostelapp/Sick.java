package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.*;

public class Sick extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "Sick";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;

    private Spinner spinner;
   private Button button14;
   private CheckBox checkBreakfast, checkLunch, checkEvening, checkDinner;
   private boolean breakfast, lunch, evening, dinner;
   private FirebaseFirestore firebaseFirestore;
    private String admissionNo = "", hostel = "",room="",block="",name="";
   private String item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sick);

        firebaseFirestore = FirebaseFirestore.getInstance();
        initWidgets();
        setSpinner();
        getSharedPreference();

        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
                setData();
                Toast.makeText(getApplicationContext(), "Sick Marked For Today", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Sick.this,Home.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();

    if (item == "Custom") {
        checkBreakfast.setEnabled(true);
        checkLunch.setEnabled(true);
        checkEvening.setEnabled(true);
        checkDinner.setEnabled(true);
    }
        else
        {
            checkBreakfast.setEnabled(false);
            checkLunch.setEnabled(false);
            checkEvening.setEnabled(false);
            checkDinner.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void initWidgets(){
        spinner = (Spinner)findViewById(R.id.spinner);
        button14=(Button)findViewById(R.id.button14);
        checkBreakfast =findViewById(R.id.checkBox);
        checkLunch =findViewById(R.id.checkBox2);
        checkEvening =findViewById(R.id.checkBox3);
        checkDinner =findViewById(R.id.checkBox4);

        checkBreakfast.setEnabled(false);
        checkLunch.setEnabled(false);
        checkEvening.setEnabled(false);
        checkDinner.setEnabled(false);
    }

    public void setSpinner(){
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Full Day");
        categories.add("Custom");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void getData(){
        if (checkBreakfast.isChecked()){
            breakfast = true;
        }
        if (checkLunch.isChecked()){
            lunch = true;
        }
        if (checkEvening.isChecked()){
            evening = true;
        }
        if (checkDinner.isChecked()){
            dinner = true;
        }
    }

    public void setData(){
        CollectionReference collectionReference = firebaseFirestore.collection("inmates").document(hostel).collection("sick");
        Map<String, Object> sick = new HashMap<>();
        if (item == "Custom"){
            if (breakfast){
                sick.put("breakfastadmno", FieldValue.arrayUnion(admissionNo));
                sick.put("breakfastname", FieldValue.arrayUnion(name));
                sick.put("breakfastroom", FieldValue.arrayUnion(room));
                sick.put("breakfastblock", FieldValue.arrayUnion(block));
                          }
             if (lunch){
                sick.put("lunchadmno", FieldValue.arrayUnion(admissionNo));
                 sick.put("lunchname", FieldValue.arrayUnion(name));
                 sick.put("lunchroom", FieldValue.arrayUnion(room));
                 sick.put("lunchblock", FieldValue.arrayUnion(block));
            }
            if (evening){
                sick.put("eveningadmno", FieldValue.arrayUnion(admissionNo));
                sick.put("eveningname", FieldValue.arrayUnion(name));
                sick.put("eveningroom", FieldValue.arrayUnion(room));
                sick.put("eveningblock", FieldValue.arrayUnion(block));
            }
             if (dinner){
                sick.put("dinneradmno", FieldValue.arrayUnion(admissionNo));
                 sick.put("dinnername", FieldValue.arrayUnion(name));
                 sick.put("dinnerroom", FieldValue.arrayUnion(room));
                 sick.put("dinnerblock", FieldValue.arrayUnion(block));
            }

            if (breakfast||lunch||evening||dinner){
                Date date = setDate();
                sick.put("date", date);
                collectionReference.document(date + "").set(sick, SetOptions.merge());
            }
        }
        else{
            Date date = setDate();
            sick.put("breakfastadmno", FieldValue.arrayUnion(admissionNo));
            sick.put("breakfastname", FieldValue.arrayUnion(name));
            sick.put("breakfastroom", FieldValue.arrayUnion(room));
            sick.put("breakfastblock", FieldValue.arrayUnion(block));
            sick.put("lunchadmno", FieldValue.arrayUnion(admissionNo));
            sick.put("lunchname", FieldValue.arrayUnion(name));
            sick.put("lunchroom", FieldValue.arrayUnion(room));
            sick.put("lunchblock", FieldValue.arrayUnion(block));
            sick.put("eveningadmno", FieldValue.arrayUnion(admissionNo));
            sick.put("eveningname", FieldValue.arrayUnion(name));
            sick.put("eveningroom", FieldValue.arrayUnion(room));
            sick.put("eveningblock", FieldValue.arrayUnion(block));
            sick.put("dinneradmno", FieldValue.arrayUnion(admissionNo));
            sick.put("dinnername", FieldValue.arrayUnion(name));
            sick.put("dinnerroom", FieldValue.arrayUnion(room));
            sick.put("dinnerblock", FieldValue.arrayUnion(block));
            sick.put("date", date);
            collectionReference.document(date+"").set(sick, SetOptions.merge());
        }
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

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNo = sharedPreferences.getString("admissionno", "");
        name = sharedPreferences.getString("name", "");
        block = sharedPreferences.getString("block", "");
        room = sharedPreferences.getString("room", "");
    }




}
