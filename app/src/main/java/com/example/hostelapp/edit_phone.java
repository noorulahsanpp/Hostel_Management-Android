package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Utils.FirebaseMethods;

public class edit_phone extends AppCompatActivity {
    TextView editPhoneTV;
    String newphone;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    Button EditBtn;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String phoneNumber;
    SharedPreferences sharedPreferences;
    private String hostel, admissionNumber, email;
    public static String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        getSupportActionBar().setTitle("Edit Phone No");
        popup();
        editPhoneTV = findViewById(R.id.editphone);
        EditBtn = findViewById(R.id.editPhoneBtn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               newphone =  editPhoneTV.getText().toString();
                if (TextUtils.isEmpty(newphone) ){
                    Toast.makeText(edit_phone.this, "Please enter new mobile no.", Toast.LENGTH_SHORT).show();
                } else {
                DocumentReference documentReference1 = firebaseFirestore.collection("inmates").document(hostel+"").collection("users").document(admissionNumber+"");
                documentReference1.update("phone",newphone);
                Toast.makeText(getApplicationContext(), "Mobile no. updated", Toast.LENGTH_LONG).show();
                finish();
            }}
        });
    }
    private void popup() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.3));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        getWindow().setAttributes(params);
    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
        phoneNumber = sharedPreferences.getString("phone", "");
        email = sharedPreferences.getString("email", "");
    }
}