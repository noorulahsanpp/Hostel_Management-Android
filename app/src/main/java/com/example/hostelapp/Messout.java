package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Utils.FirebaseMethods;

public class Messout extends AppCompatActivity {

    private TextView Frmbtn, Tobtn;
    private Button Okbtn;
    private EditText txtfrm, txtTo , days;
   // private TextView days;
    private int mYear, mMonth, mDay;
    private Date dateObj1, dateObj2;
    private String fromDate;
    private String toDate;
    private int month1,month2;


    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private FirebaseMethods firebaseMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_messout);
        Frmbtn = (TextView) findViewById(R.id.txtfrom);
        Tobtn = (TextView) findViewById(R.id.txtto);
        Okbtn = (Button) findViewById(R.id.button3);
        //txtfrm = (EditText) findViewById(R.id.edittext14);
        //txtTo = (EditText) findViewById(R.id.edittext15);
        days = (EditText) findViewById(R.id.edittext13);


        mContext = Messout.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);


       /*  pop up
       DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);*/


        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");


        Frmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);




                final DatePickerDialog datePickerDialog =  new DatePickerDialog(Messout.this,new DatePickerDialog.OnDateSetListener() {

                  @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Frmbtn.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        fromDate = dayOfMonth + " " + (month + 1) + " " + year;
                        month1 = month;
                        try {
                            dateObj1 = simpleDateFormat.parse(fromDate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
               datePickerDialog.show();
            }
        });


        Tobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =  new DatePickerDialog(Messout.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Tobtn.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        toDate = dayOfMonth + " " + (month + 1) + " " + year;
                        month2= month;
                        System.out.println("ToDate : "+toDate);
                        if(month1==month2)
                        {
                            try {

                            dateObj2 = simpleDateFormat.parse(toDate);
                            long diff = dateObj2.getTime()-dateObj1.getTime();
                            int dateDiff = ((int) (diff / (24 * 60 * 60 * 1000)))+1;
                            if (dateDiff > 15) {
                                Toast.makeText(getBaseContext(), " cannot select more than 15 days", Toast.LENGTH_LONG).show();
                                days.setText("");
                                Tobtn.setText("");
                            }
                            else {
                                days.setText(" " + dateDiff);
                            }
                        }

                        catch (ParseException e) {
                            e.printStackTrace();
                        }}
                        else{
                            Toast.makeText(getBaseContext(), "Select current month", Toast.LENGTH_LONG).show();
                            days.setText("");
                            Tobtn.setText("");


                        }


                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(dateObj1.getTime());
                datePickerDialog.show();

            }
        });

        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date from = dateObj1;
                Date to = dateObj2;
                String ndays = days.getText().toString();

                if (ndays.matches("")) {
                    Toast.makeText(getBaseContext(), "Select valid date", Toast.LENGTH_LONG).show();

                } else {

                    documentReference = firebaseFirestore.collection("inmates").document("LH").collection("users").document("LH002").collection("messout").document("june");
                    Map<String, Object> messout = new HashMap<>();
                    messout.put("from", from);
                    messout.put("to", to);
                    messout.put("days", ndays);
                    documentReference.set(messout);
                    startActivity(new Intent(Messout.this, Home.class));
                }
            }
        });
    }
}



