package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class changepassword extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = "changepassword";
    private EditText currentEt,newpswdEt,confirmEt;
    private Button changepswd;
    private FirebaseAuth auth;
    private ProgressDialog progressdialog;
    private SharedPreferences sharedPreferences;
    private String newPassword, oldPassword, confirmPassword, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        initWidgets();

        auth = FirebaseAuth.getInstance();
        popup();

        getSharedPreference();
        progressdialog = new ProgressDialog(changepassword.this);

        changepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressdialog.setMessage("Please wait");
                progressdialog.show();
               getData();
                validatePassword();
            }});



       DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
       int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
       params.gravity= Gravity.CENTER;
       params.x=0;
        params.y=-20;
       getWindow().setAttributes(params);

    }

    public void validatePassword(){

        if(oldPassword.length()==0) {
            currentEt.setError("Enter current password");
            //  currentEt.requestFocus();
            return;
        }
        if(newPassword.length()==0)  {
            newpswdEt.setError("Enter new password");
            //   newpswdEt.requestFocus();
            return;
        }
        if(confirmPassword.length()==0) {
            confirmEt.setError("Confirm new password");
            //   confirmEt.requestFocus();
            return;
        }
        if(newPassword.equals(confirmPassword)){
            changepwd();
        }
        else {
            confirmEt.setError("Password mismatching");
            confirmEt.requestFocus();
            return;
        }
    }

    private void changepwd() {

    FirebaseUser user = auth.getCurrentUser();
    if(user!=null)
    {

        AuthCredential credential = EmailAuthProvider
                .getCredential(email,oldPassword);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User password updated.");
                                                Toast.makeText(getApplicationContext(), "Password updated.", Toast.LENGTH_LONG).show();
                                                finish();

                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Re-authentication failed ,Please try again", Toast.LENGTH_LONG).show();
                        }
                        progressdialog.dismiss();
                    }
                });
    }}

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
    }

    public void initWidgets(){
        currentEt=findViewById(R.id.current);
        newpswdEt=findViewById(R.id.newpswd);
        confirmEt=findViewById(R.id.confirm);
        changepswd=findViewById(R.id.button15);
    }

    public void getData(){
        oldPassword = currentEt.getText().toString().trim();
        newPassword = newpswdEt.getText().toString().trim();
        confirmPassword = confirmEt.getText().toString().trim();
    }

    private void popup() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);

    }

}
