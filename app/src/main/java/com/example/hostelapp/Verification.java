package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Utils.FirebaseMethods;

public class Verification extends AppCompatActivity {
    private static final String TAG = "Verification";
    private EditText num;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button check;
    private String adnumber,regn;
    private DocumentReference documentReference;
    private FirebaseMethods firebaseMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verification);

        num = findViewById(R.id.editText5);
        check = findViewById(R.id.button15);
        mContext = Verification.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);

        popup();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = num.getText().toString().trim();
                verify(number);
            }
        });
    }
    private void popup() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);
    }
    private void verify(String number) {
        documentReference = firebaseFirestore.collection("registered").document(number);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){

                        regn = document.get("app_reg").toString();
                        if(regn.equals("yes"))
                        {
                            Toast.makeText(getApplicationContext(),"Already registered", Toast.LENGTH_LONG).show();

                        }
                        else {

                            Toast.makeText(getApplicationContext(), "" + document.getData(), Toast.LENGTH_LONG).show();
                            adnumber = document.getId().toString();

                            Intent intent = new Intent(Verification.this, UserRegistration.class);
                            intent.putExtra("adnumber", adnumber);
                            startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Invalid registration number", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
