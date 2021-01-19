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
import java.util.List;
import Utils.FirebaseMethods;

public class Fees extends AppCompatActivity implements  PaymentResultListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = "";
    Spinner year;
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
    int totalFee = 2500,presentdays,smonth,syear;
    public static String date;
    int kk = 0;

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
                                month.setText(smonth+"/"+syear);
                                showtotal();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(2018)
                        .setActivatedYear(actyear)
                        .setMaxYear(actyear)
                        .setMinMonth(Calendar.FEBRUARY)
                        .setTitle("Select Month and Year")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {
                                smonth = selectedMonth+1;
                            } })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int selectedYear) {
                                syear = selectedYear+1;
                            } })
                        .build()
                        .show();
            }
        });


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        date = syear + "-0" + smonth;
        getabs();
        // DocumentReference documentReference= firebaseFirestore.collection("inmates").document("LH").collection("fee").document(date);
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
                    common.setText(Common+"");
                    rent.setText(Rent+"");
                    extras.setText("75");
                    //     due.setText(duedate);
                    int ttotal = (presentdays*Integer.parseInt(messfee))+Integer.parseInt(Common)+Integer.parseInt(Rent)+75;
                    total.setText(ttotal+"");
                    duedate.setText(Duedate+"");
                    presentdays = days - Integer.parseInt(absent);
                    kk = presentdays * Integer.parseInt(messfee);
                    mess.setText(kk+"");

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
                }
                else{
                    status.setText("Pending");
                }
            }
        });

    }
    public void payWithStripe(){
        Intent intent= new Intent(Fees.this, CheckoutActivityJava.class);
        intent.putExtra("amount", totalFee);
        startActivity(intent);
    }


    public void startPayment() {
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
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", email+"");
            options.put("prefill.contact",phoneNumber+"");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    private void initWidgets() {


        month = findViewById(R.id.month);
        year = (Spinner) findViewById(R.id.spinneryear);
        status = (TextView) findViewById(R.id.txtstatus);
        rent = (TextView) findViewById(R.id.rent);
        extras = (TextView) findViewById(R.id.extras);
        common = (TextView) findViewById(R.id.common);
        //    due = (TextView) findViewById(R.id.txtdue);
        duedate = (TextView) findViewById(R.id.txtduedate);
        total = (TextView) findViewById(R.id.txttotal);
        gtotal = (TextView) findViewById(R.id.txtgrand);
        mess = (TextView) findViewById(R.id.mess);
        payBtn = findViewById(R.id.btnpay);
    }
//    private void setAdapter()
//    {
//        ArrayList<String> years = new ArrayList<String>();
//        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
//        for (int j = 2018; j <= 2025; j++) {
//            years.add(Integer.toString(j));
//        }
//
//
//        ArrayAdapter<String> yearadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
//        year.setAdapter(yearadapter);
//        List<String> monthsList = new ArrayList<String>();
//        String[] months = new DateFormatSymbols().getMonths();
//        for (int i = 0; i < months.length; i++) {
//            monthsList.add(months[i]);
//        }
//        ArrayAdapter<String> monthadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
//        month.setAdapter(monthadapter);
//        month.setOnItemSelectedListener(this);
//        year.setOnItemSelectedListener(this);
//    }
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        selectedYear = year.getSelectedItem().toString();
//        String months1 = month.getSelectedItem().toString();
//        String[] months = new DateFormatSymbols().getMonths();
//        for (int i = 0; i < months.length; i++) {
//            if (months1 == months[i]) {
//                monthIndex = String.valueOf(i+1);
//                break;
//            }}
//
//        date = selectedYear + "-0" + monthIndex;
//        System.out.println(date);
//
//
//
//        Calendar cal = Calendar.getInstance();
//        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        getabs();
//        // DocumentReference documentReference= firebaseFirestore.collection("inmates").document("LH").collection("fee").document(date);
//        DocumentReference documentReference= firebaseFirestore.collection("inmates").document(hostel+"").collection("fee").document(date+"");
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    messfee = document.get("veg").toString();
//                    String Common = document.get("common").toString();
//                    String  Rent = document.get("rent").toString();
//                    String Duedate = document.get("duedate").toString();
//                    common.setText(Common+"");
//                    rent.setText(Rent+"");
//                    extras.setText("75");
//                    //     due.setText(duedate);
//                    int ttotal = (presentdays*Integer.parseInt(messfee))+Integer.parseInt(Common)+Integer.parseInt(Rent)+75;
//                    total.setText(ttotal+"");
//                    duedate.setText(Duedate+"");
//                    presentdays = days - Integer.parseInt(absent);
//                    kk = presentdays * Integer.parseInt(messfee);
//                    mess.setText(kk+"");
//
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "Doesn't Exist", Toast.LENGTH_LONG).show();
//                    gtotal.setText("");
//                    common.setText("");
//                    rent.setText("");
//                    mess.setText("");
//                    extras.setText("");
//                    total.setText("");
//                    gtotal.setText("");
//                    duedate.setText("");
//                    status.setText("");
//                    duedate.setText("");
//                }
//            }
//        });
//        DocumentReference documentReference1 = firebaseFirestore.collection("inmates").document(hostel+"").collection("users").document(admissionNumber+"").collection("fees").document(date+"");
//        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot documentSnapshot = task.getResult();
//                if (documentSnapshot.exists()) {
//                    status.setText("Paid");
//                }
//                else{
//                    status.setText("Pending");
//                }
//            }
//        });
//
//    }
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }


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
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
    }

    public void getabs(){
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
    }
}