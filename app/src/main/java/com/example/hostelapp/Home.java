package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Utils.BottomNavigationViewHelper;
import models.User;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private static final String KEY_ADMISSIONNO = "admission_number";
    private static final String KEY_HOSTEL = "hostel";
    private static final String KEY_USERID = "user_dd";
    private static final int ACTIVITY_NUM = 0;
    public static final String MyPREFERENCES = "MyPrefs";


    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    private String userName, hostel, admissionNumber, name;
    private TextView userNameView;
    private ImageView profilePicture;
    private Task<DocumentSnapshot> documentReference;
    private StorageReference storageReference;
    private SharedPreferences sharedPreferences;
    private CardView fees,sick,attendance,messout,menu;
    private Boolean exit = false;
    SharedPreferences sharedPreferences;
    private CardView fees, sick, attendance, messout, menu;
    private DatabaseReference databaseReference;
    private List<SlideModel> slideLists;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting");
        //     setupBottomNavigationView();
        final int[] images = new int[]{};

        initialize();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        slideLists = new ArrayList<>();


        //     viewFlipper.setFlipInterval(2000);
        //      viewFlipper.startFlipping();
        //       viewFlipper.setAutoStart(true);
        //     Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
//        viewFlipper.setInAnimation(in);

        //  mAuth.signOut();

        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        getSharedPreference();
        onTokenRefresh();


        StorageReference profileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile_picture/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                setProfilePicture(uri);
            }
        });

    /*    final ImageView imageView[]= new ImageView[100];
        final StorageReference imageRef = storageReference.child("LH/");
    imageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
        @Override
        public void onSuccess(ListResult listResult) {
            for (StorageReference item: listResult.getItems()){
                for (int i = 0; i < listResult.getItems().size(); i++) {

                    Picasso.get().load((Uri) listResult.getItems()).into(imageView[i]);

                }
                for (int i = 0; i < listResult.getItems().size(); i++) {
                    viewFlipper.addView(imageView[i]);
                    Picasso.get().load(String.valueOf(item)).into(imageView[10]);
                }
            }

        }
    });*/



  /*    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
      public void onSuccess(Uri uri) {
         ImageView imageView = new ImageView(getApplicationContext());
                   Picasso.get().load(uri).into(imageView);
                viewFlipper.addView(imageView);
        }
        });*/


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                intent.putExtra("hostel", hostel);
                intent.putExtra("admission_number", admissionNumber);
                startActivity(intent);



            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        usingFirebaseDatabase();
    }

    private void usingFirebaseDatabase() {
        databaseReference.child("uploads")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            slideLists.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SlideModel model = (SlideModel) snapshot.getValue();

                                slideLists.add(model);
                            }
                            Toast.makeText(Home.this, "All data fetched", Toast.LENGTH_SHORT).show();
                            usingFirebaseImages(slideLists);
                        } else {
                            Toast.makeText(Home.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Home.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void usingFirebaseImages(List<SlideModel> slideLists) {
        for (int i = 0; i < slideLists.size(); i++) {
            String downloadImageUrl = slideLists.get(i).getImageUrl();
            flipImages(downloadImageUrl);
        }
    }

    public void flipImages(String imageUrl) {
        ImageView imageView = new ImageView(this);
        Picasso.get().load(imageUrl).into(imageView);

        viewFlipper.addView(imageView);

        viewFlipper.setFlipInterval(2500);
        viewFlipper.setAutoStart(true);

        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    private void initialize() {
        viewFlipper = findViewById(R.id.viewflipper);
    }

    private void setProfilePicture(Uri uri) {
        Picasso.get().load(uri).into(profilePicture);
    }


    private void initWidgets() {
        profilePicture = findViewById(R.id.imageView);
        userNameView = findViewById(R.id.textView5);
        logoutBtn = (Button) findViewById(R.id.button2);
        //  NotificationBtn = (Button) findViewById(R.id.button5);
        attendance = (CardView) findViewById(R.id.attendance);
        fees = (CardView) findViewById(R.id.fees);
        sick = (CardView) findViewById(R.id.sick);
        menu = (CardView) findViewById(R.id.menu);
        messout = (CardView) findViewById(R.id.messout);

    }


    /*  private void setupBottomNavigationView(){
          Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");
          BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
          BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
          BottomNavigationViewHelper.enableNavigation(Home.this,bottomNavigationView);
         android.view.Menu menu = bottomNavigationView.getMenu();
         MenuItem menuItem = menu.getItem( ACTIVITY_NUM );
         menuItem.setChecked(true);

      }*/
    @Override
    protected void onResume() {
        super.onResume();
        getSharedPreference();
    }

    public void getSharedPreference() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        admissionNumber = sharedPreferences.getString("admissionno", "");
    }

    private class SlideModel {
        private String imageUrl, name;

        public SlideModel() {
        }

        public SlideModel(String imageUrl, String name) {
            this.imageUrl = imageUrl;
            this.name = name;
        }


        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }
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
