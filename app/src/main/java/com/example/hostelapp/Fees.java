package com.example.hostelapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.CompoundButton;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.FirebaseMethods;

public class Fees extends AppCompatActivity implements  PaymentResultListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = "";
    Spinner year;
    CheckBox paydue;
    TextView mess, rent, extras, common, due, total, gtotal,duedate,status,month;
    Button payBtn;
    String monthIndex,selectedYear;
    String messfee ,absent="0";
    String phoneNumber;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    SharedPreferences sharedPreferences;
    private String hostel, admissionNumber, email;
    int totalFee,presentdays,smonth,syear;
    public static String date;
    int kk = 0;
    int grandtotal=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fees);
        Checkout.preload(getApplicationContext());
        mContext = Fees.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        initWidgets();
        //   setAdapter();
        getSharedPreference();

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                int actyear =today.get(Calendar.YEAR);
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Fees.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                smonth = selectedMonth+1;
                                syear = selectedYear;
                                if(smonth>9){
                                    date = syear + "-" + smonth;
                                }
                                else{
                                    date = syear + "-0" + smonth;
                                }
                                month.setText(date+"");
                                showtotal();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(2018)
                        .setActivatedYear(actyear)
                        .setMaxYear(actyear)
                        .setMinMonth(Calendar.FEBRUARY)
                        .setTitle("Choose a Month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {

                            } })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int selectedYear) {
                            } })
                        .build()
                        .show();
            }
        });

        paydue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked ) {
                    due.setText("1500");
                    grandtotal = 1500 + totalFee;
                    gtotal.setText(grandtotal + "");
                }
                else {
                    due.setText("");
                    gtotal.setText(totalFee+"");
                }
            }
        });
        paydue.setEnabled(false);
        due.setEnabled(false);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(smonth>9){
                    date = syear + "-" + smonth;
                }
                else{
                    date = syear + "-0" + smonth;
                }
                DocumentReference documentReference = firebaseFirestore.collection("inmates").document(hostel).collection("users").document(admissionNumber).collection("fees").document(date);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(),"Already Paid For this", Toast.LENGTH_LONG).show();
                        }
                        else {
                            //                payWithStripe();
                            startPayment();
                        }
                    }
                });

            }
        });

    }
    public void showtotal(){
        Calendar cal = Calendar.getInstance();
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(smonth>9){
            date = syear + "-" + smonth;
        }
        else{
            date = syear + "-0" + smonth;
        }
        DocumentReference documentReference2 = firebaseFirestore.collection("inmates").document(hostel+"").collection("users").document(admissionNumber+"").collection("attendance").document(date+"");
        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                    absent = documentSnapshot.get("daysabsent").toString();
                }

            }
        });

        DocumentReference documentReference= firebaseFirestore.collection("inmates").document(hostel+"").collection("fee").document(date+"");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    messfee = document.get("veg").toString();
                    String Common = document.get("common").toString();
                    String  Rent = document.get("rent").toString();
                    String Duedate = document.get("duedate").toString();
                    presentdays = days - Integer.parseInt(absent);
                    kk = presentdays * Integer.parseInt(messfee);
                    totalFee = (presentdays*Integer.parseInt(messfee))+Integer.parseInt(Common)+Integer.parseInt(Rent)+75;
                    mess.setText(kk+"");
                    common.setText(Common+"");
                    rent.setText(Rent+"");
                    extras.setText("75");
                    total.setText(totalFee+"");
                    gtotal.setText(totalFee+"");
                    duedate.setText(Duedate+"");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Doesn't Exist", Toast.LENGTH_LONG).show();
                    gtotal.setText("");
                    common.setText("");
                    rent.setText("");
                    mess.setText("");
                    extras.setText("");
                    total.setText("");
                    gtotal.setText("");
                    duedate.setText("");
                    status.setText("");
                    duedate.setText("");
                }
            }
        });
        DocumentReference documentReference1 = firebaseFirestore.collection("inmates").document(hostel+"").collection("users").document(admissionNumber+"").collection("fees").document(date+"");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    status.setText("Paid");
                    paydue.setEnabled(false);
                    due.setEnabled(false);
                }
                else{
                    status.setText("Pending");
                    paydue.setEnabled(true);
                    due.setEnabled(true);
                }
            }
        });

    }
    public void payWithStripe(){
        Intent intent= new Intent(Fees.this, CheckoutActivityJava.class);
        intent.putExtra("amount", grandtotal+"");
        startActivity(intent);
    }


    public void startPayment() {
        grandtotal = Integer.parseInt(gtotal.getText().toString())*100;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_NoNpIryvAqtM1p");
        /**
         * Instantiate Checkout
         */


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "CET Hostel");
            options.put("description", "Hostel : "+hostel + "Admission Number : "+admissionNumber);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", grandtotal+"");//pass amount in currency subunits
            options.put("prefill.email", email+"");
            options.put("prefill.contact",phoneNumber+"");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    private void initWidgets() {

paydue = findViewById(R.id.paydue);
        month = findViewById(R.id.month);
        year = (Spinner) findViewById(R.id.spinneryear);
        status = (TextView) findViewById(R.id.txtstatus);
        rent = (TextView) findViewById(R.id.rent);
        extras = (TextView) findViewById(R.id.extras);
        common = (TextView) findViewById(R.id.common);
        due = (TextView) findViewById(R.id.txtdue);
        duedate = (TextView) findViewById(R.id.txtduedate);
        total = (TextView) findViewById(R.id.txttotal);
        gtotal = (TextView) findViewById(R.id.txtgrand);
        mess = (TextView) findViewById(R.id.mess);
        payBtn = findViewById(R.id.btnpay);
    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
        phoneNumber = sharedPreferences.getString("phone", "");
        email = sharedPreferences.getString("email", "");
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
        DocumentReference documentReference1 = firebaseFirestore.collection("inmates").document(hostel+"").collection("users").document(admissionNumber+"").collection("fees").document(date+"");
        Map<String, Object> status = new HashMap<>();
        status.put("status","paid");
        documentReference1.set(status);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
    }

}