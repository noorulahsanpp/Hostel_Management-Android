package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Messout extends AppCompatActivity {

    private TextView Frmbtn, Tobtn;
    private Button Okbtn;
    private EditText txtfrm, txtTo , days;
   // private TextView days;
    private int mYear, mMonth, mDay;
    private Date dateObj1, dateObj2;
    private String fromDate, toDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_messout);
        Frmbtn = (TextView) findViewById(R.id.button10);
        Tobtn = (TextView) findViewById(R.id.button11);
        Okbtn = (Button) findViewById(R.id.button3);
        //txtfrm = (EditText) findViewById(R.id.edittext14);
        //txtTo = (EditText) findViewById(R.id.edittext15);
        days = (EditText) findViewById(R.id.edittext13);


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

                DatePickerDialog datePickerDialog =  new DatePickerDialog(Messout.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Frmbtn.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        fromDate = dayOfMonth + " " + (month + 1) + " " + year;
                        try {
                            dateObj1 = simpleDateFormat.parse(fromDate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        Tobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =  new DatePickerDialog(Messout.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Tobtn.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        toDate = dayOfMonth + " " + (month + 1) + " " + year;
                        System.out.println("ToDate : "+toDate);
                        try {
                            dateObj2 = simpleDateFormat.parse(toDate);
                            long diff = dateObj2.getTime()-dateObj1.getTime();
                            int dateDiff = (int) (diff / (24 * 60 * 60 * 1000));
                            days.setText(" "+dateDiff);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });





        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Messout.this, Home.class));
            }
        });
    }
}



