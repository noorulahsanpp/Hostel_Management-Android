package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Utils.FirebaseMethods;

public class Messout extends AppCompatActivity {

    private static final String TAG = "Messout";
    private TextView txtFrm, txtTo;
    private Button Okbtn;
    private EditText days;
    private int mYear, mMonth, mDay;
    private Date dateObj1, dateObj2;
    private String fromDate, toDate;
    private int month1,month2;

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private FirebaseMethods firebaseMethods;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_messout);
       txtFrm = (TextView) findViewById(R.id.txtfrom);
       txtTo = (TextView) findViewById(R.id.txtto);
        Okbtn = (Button) findViewById(R.id.button3);
        days = (EditText) findViewById(R.id.edittext13);


        mContext = Messout.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);

        txtFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromDate();
            }
        });


        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToDate();
            }
        });

        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMessout();
            }
        });
    }
    public void setFromDate(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog =  new DatePickerDialog(Messout.this,new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtFrm.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void setToDate(){
        if (txtFrm.getText() == " ") {
            Toast.makeText(getBaseContext(), " Please select from date", Toast.LENGTH_LONG).show();
        }
        else {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Messout.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    txtTo.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    toDate = dayOfMonth + " " + (month + 1) + " " + year;
                    month2 = month;
                    System.out.println("ToDate : " + toDate);
                    if (month1 == month2) {
                        try {

                            dateObj2 = simpleDateFormat.parse(toDate);
                            long diff = dateObj2.getTime() - dateObj1.getTime();
                            int dateDiff = ((int) (diff / (24 * 60 * 60 * 1000))) + 1;
                            if (dateDiff > 15) {
                                Toast.makeText(getBaseContext(), " cannot select more than 15 days", Toast.LENGTH_LONG).show();
                                days.setText("");
                                txtTo.setText("");
                            } else {
                                days.setText(" " + dateDiff);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Select current month", Toast.LENGTH_LONG).show();
                        days.setText("");
                        txtTo.setText("");


                    }


                }
            }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMinDate(dateObj1.getTime());
            datePickerDialog.show();

        }

    }

    public void setMessout(){
        String ndays = days.getText().toString();
        if (ndays.matches("")) {
            Toast.makeText(getBaseContext(), "Select valid date", Toast.LENGTH_LONG).show();
        }
        else {
            Date startDate = new Date();
            Date endDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
                saveData(start, end);
        }
    }

    public void saveData(Calendar start, Calendar end){
        documentReference = firebaseFirestore.collection("inmates").document("LH");
        Map<String, Object> messout = new HashMap<>();
        System.out.print("Date does not exist");
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            messout.put("date", new Timestamp(date));
            messout.put("absents", FieldValue.arrayUnion("LH001"));
            messout.put("total_absentees", FieldValue.increment(1));
            documentReference.collection("attendance").document(date + "").set(messout, SetOptions.merge());
        }
    }
}



