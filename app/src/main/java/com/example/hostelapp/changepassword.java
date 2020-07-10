package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

    private static final String TAG = "changepassword";
    private EditText currentEt,newpswdEt,confirmEt;
    private Button changepswd;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        currentEt = findViewById(R.id.current);
        newpswdEt = findViewById(R.id.newpswd);
        confirmEt = findViewById(R.id.confirm);
        changepswd = findViewById(R.id.button15);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        popup();


        changepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEt.getText().toString().length() == 0) {
                    currentEt.setError("Enter current password");
                    return;
                }
                if (newpswdEt.getText().toString().length() == 0) {
                    newpswdEt.setError("Enter new password");
                    return;
                }
                if (confirmEt.getText().toString().length() == 0) {
                    confirmEt.setError("Confirm new password");
                    return;
                }
                if (newpswdEt.getText().toString().equals(confirmEt.getText().toString())) {
                    changepwd();
                } else {
                    confirmEt.setError("Password mismatching");
                    confirmEt.requestFocus();
                    return;

                }
            }
        });
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



    private void changepwd() {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if(user!=null)
    {
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(),currentEt.getText().toString());
// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");
                            Toast.makeText(getApplicationContext(), "Re-authentication success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String newPassword = newpswdEt.getText().toString();

                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User password updated.");
                                                Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Error password not updates", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Re-authentication failed ,Please try again", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    else{
        Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_LONG).show();
    }
    }


}
