package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class edit_email extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    TextView oldEmail;
    Button EditBtn;
    private String emailSp;
    SharedPreferences sharedPreferences;
    EditText editEmailTV,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        getSupportActionBar().setTitle("Edit Email");
        popup();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        editEmailTV = findViewById(R.id.editemail);
        EditBtn = findViewById(R.id.EditBtn);
        password = findViewById(R.id.passwrdET);
        oldEmail = findViewById(R.id.oldEmail);
        getSharedPreference();
        oldEmail.setText(emailSp);

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMail = editEmailTV.getText().toString().trim();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(emailSp, password.getText().toString()); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(newMail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Email added", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                //----------------------------------------------------------\\
                            }
                        });

            }
        });

    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        emailSp = sharedPreferences.getString("email", "");
    }
    private void popup() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        getWindow().setAttributes(params);
    }
}