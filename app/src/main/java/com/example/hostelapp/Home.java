package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Utils.BottomNavigationViewHelper;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private static final String KEY_ADMISSIONNO = "admission_number";
    private static final String KEY_HOSTEL = "hostel";
    private static final String KEY_USERID = "user_dd";
    private static final int ACTIVITY_NUM = 0;
    public static final String MyPREFERENCES = "MyPrefs" ;


    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    private String userName,hostel, admissionNumber, name;
    private TextView userNameView;
    private StorageReference storageReference;
    private SharedPreferences sharedPreferences;
    private CardView fees,sick,attendance,messout,menu;
    private Boolean exit = false;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate: starting");
        setupBottomNavigationView();
        int[] images =new int[]{};

        viewFlipper = findViewById(R.id.viewflipper);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();
        viewFlipper.setAutoStart(true);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(in);

        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(images[i]);
            viewFlipper.addView(imageView);
        }
        initWidgets();

       mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        getSharedPreference();
        onTokenRefresh();


       /* profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                intent.putExtra("hostel", hostel);
                intent.putExtra("admission_number", admissionNumber);
                startActivity(intent);



            }
        });*/

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                finish();
                startActivity(new Intent(Home.this, Login.class));

            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Attendance.class));
            }
        });
        fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Fees.class));
            }
        });

        sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Sick.class));
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Menu.class);
                startActivity(intent);

            }
        });

        messout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Messout.class));
            }
        });
    }


    private void initWidgets(){
        userNameView = findViewById(R.id.textView5);
        logoutBtn = (Button) findViewById(R.id.button2);
        attendance= (CardView) findViewById(R.id.attendance);
        fees= (CardView) findViewById(R.id.fees);
        sick = (CardView) findViewById(R.id.sick);
        menu = (CardView) findViewById(R.id.menu);
        messout = (CardView)findViewById(R.id.messout);

    }

   /* private void setUserData(String name){
        userNameView.setText("Welcome " + name);
        StorageReference profileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile_picture/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });
    }*/
    private void setupBottomNavigationView(){
        Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(Home.this,bottomNavigationView);
       android.view.Menu menu = bottomNavigationView.getMenu();
       MenuItem menuItem = menu.getItem( ACTIVITY_NUM );
       menuItem.setChecked(true);

    }
    @Override
    protected void onResume(){
        super.onResume();
        getSharedPreference();
    }

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
