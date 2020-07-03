package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Utils.FirebaseMethods;

public class Messout extends AppCompatActivity {

    private static final String TAG = "Messout";
    public static final String MyPREFERENCES = "MyPrefs";
    private TextView txtFrm, txtTo;
    private Button Okbtn;
    private EditText days;
    private int mYear, mMonth, mDay;
    private Date dateObj1, dateObj2;
    private String fromDate, toDate, hostel, admissionNo;
    private int month1,month2;

    private Context mContext;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    private CollectionReference collectionReference;
    private int flag = 0;
    private ArrayList<Date> dd;
    private AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_messout);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        initWidgets();
        getSharedPreference();

        mContext = Messout.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        collectionReference = firebaseFirestore.collection("inmates").document(hostel).collection("attendance");
        dd = new ArrayList<>();
        builder = new AlertDialog.Builder(this);
        getData();

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
                if (txtFrm.getText()==""||txtTo.getText()==""){
                    toastMessage(" Invalid date.");
                    return;
                }
                String from1;
                String to1;
                from1 = new SimpleDateFormat("EEE, MMM d").format(dateObj1);
                to1 = new SimpleDateFormat("EEE, MMM d").format(dateObj2);

                builder.setMessage("Do you want to mark messout from "+from1+" to "+to1+" ?" ).setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setMessout();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Messout");
                alertDialog.show();
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
            toastMessage(" Please select from date");
            txtFrm.requestFocus();
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
                                toastMessage("cannot select more than 15 days");
                                days.setText("");
                                txtTo.setText("");
                            } else {
                                days.setText(" " + dateDiff);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        toastMessage("Select current month");
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
            toastMessage("Select a valid date");
        }
        else {
            Date startDate = new Date();
            Date endDate = new Date();
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
            validate(start, end);
            if (flag == 1){
                toastMessage(" Attendance already marked in this date");
                flag = 0;
            }
            else
            {
                saveData();
            }
        }
    }

    public void validate(@NotNull Calendar start, Calendar end){
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            if (dd.contains(date)){
                flag = 1;
                break;
            }
        }
    }

    public void saveData(){
        Date startDate = new Date();
        Date endDate = new Date();
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
        Map<String, Object> messout = new HashMap<>();
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            messout.put("date", date);
            messout.put("absents", FieldValue.arrayUnion(admissionNo));
            messout.put("total_absentees", FieldValue.increment(1));
            collectionReference.document(date + "").set(messout, SetOptions.merge());
        }
        toastMessage(" Messout successfully marked");
        startActivity(new Intent(Messout.this, Home.class));
    }

    public void getData(){
        Query query = collectionReference.whereArrayContains("absents", admissionNo);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        dd.add(document.getDate("date"));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void toastMessage(String message){
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNo = sharedPreferences.getString("admissionno", "");
    }

    public void initWidgets(){
        txtFrm = (TextView) findViewById(R.id.txtfrom);
        txtTo = (TextView) findViewById(R.id.txtto);
        Okbtn = (Button) findViewById(R.id.button3);
        days = (EditText) findViewById(R.id.edittext13);
    }
}