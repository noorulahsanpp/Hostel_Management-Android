package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

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
    private Spinner spinner;
   private Button button14;
   private CheckBox checkBreakfast, checkLunch, checkEvening, checkDinner;
   private boolean breakfast, lunch, evening, dinner;
   private FirebaseFirestore firebaseFirestore;
   private String item;
   private String userId ="LH002";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sick);

        firebaseFirestore = FirebaseFirestore.getInstance();
        initWidgets();
        setSpinner();

        /*pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);*/


        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
                setData();
//                startActivity(new Intent(Sick.this,Home.class));
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
    public void onNothingSelected(AdapterView<?> arg0) {

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
        CollectionReference collectionReference = firebaseFirestore.collection("inmates").document("LH").collection("sick");
        Map<String, Object> sick = new HashMap<>();
        if (item == "Custom"){
            if (breakfast){
                sick.put("breakfast", FieldValue.arrayUnion("LH002"));
            }
            else if (lunch){
                sick.put("lunch", FieldValue.arrayUnion("LH002"));
            }
            else if (evening){
                sick.put("evening", FieldValue.arrayUnion("LH002"));
            }
            else if (dinner){
                sick.put("dinner", FieldValue.arrayUnion("LH002"));
            }
            else
            {
                Toast.makeText(getBaseContext(), "Invalid input", Toast.LENGTH_LONG).show();
            }

            if (breakfast||lunch||evening||dinner){
                Date date = setDate();
                sick.put("date", date);
                collectionReference.document(date + "").set(sick, SetOptions.merge());
            }
        }
        else{
            Date date = setDate();
            sick.put("breakfast", FieldValue.arrayUnion("LH002"));
            sick.put("lunch", FieldValue.arrayUnion("LH002"));
            sick.put("evening", FieldValue.arrayUnion("LH002"));
            sick.put("dinner", FieldValue.arrayUnion("LH002"));
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



}
