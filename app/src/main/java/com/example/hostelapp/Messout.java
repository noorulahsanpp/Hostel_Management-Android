package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class Messout extends AppCompatActivity {

    private Button Frmbtn, Tobtn, Okbtn;
    private EditText txtfrm, txtTo , days;
   // private TextView days;
    private int mYear, mMonth, mDay;
  //  private Date date1, date2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messout);
        Frmbtn = (Button) findViewById(R.id.button10);
        Tobtn = (Button) findViewById(R.id.button11);
        Okbtn = (Button) findViewById(R.id.button3);
        txtfrm = (EditText) findViewById(R.id.edittext14);
        txtTo = (EditText) findViewById(R.id.edittext15);
        days = (EditText) findViewById(R.id.edittext13);


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

                        txtfrm.setText(dayOfMonth + "-" + (month + 1) + "-" + year);


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

                        txtTo.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

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



