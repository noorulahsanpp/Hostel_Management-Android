package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private static final String KEY_ADMISSIONNO = "admission_number";
    private static final String KEY_HOSTEL = "hostel";
    private static final String KEY_USERID = "user_dd";
    private static final int ACTIVITY_NUM = 0;
    public static final String MyPREFERENCES = "MyPrefs";
    private static CollectionReference collectionReference;

    private FirebaseAuth mAuth;
    private Button notification;

    private static FirebaseFirestore firebaseFirestore;
    private String userID;
    private static String userName, hostel, admissionNumber, name;
    private TextView userNameView;
    private ImageView profilePicture;
    private Task<DocumentSnapshot> documentReference;
    private StorageReference storageReference;
    private SharedPreferences sharedPreferences;
    private CardView fees,sick,attendance,messout,menu;
    private Boolean exit = false;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private ViewFlipper viewFlipper;
    static  TextView TVnotificationBadge;
    static int newnotification , count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
      getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting");

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(Home.this, Login.class));
            return;
        }

        mUploads = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        initWidgets();
        init();
        onTokenRefresh();
        getSharedPreference();

    }
//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main, menu);
//        final MenuItem menuItem = menu.findItem(R.id.ic_action_notification);
//        View actionView = menuItem.getActionView();
//        TVnotificationBadge =actionView.findViewById(R.id.cart_badge);
//        TVnotificationBadge.setVisibility(View.INVISIBLE);
//        getquantity();
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//               return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.ic_action_notification) {
//            TVnotificationBadge.setVisibility(View.INVISIBLE);
//            updateProducts();
//            startActivity(new Intent(this,Notification.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    public static void updateProducts(){
        firebaseFirestore.collection("inmates").document(hostel).collection("notification").document().update(
                "status","viewed");
    }

    public static void getquantity() {

        collectionReference = firebaseFirestore.collection("inmates").document(hostel).collection("notification");
        collectionReference.whereEqualTo("status","notviewed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                count = count+1;
                newnotification = count;
            }
        });
        setupBadge();
    }
    public static void setupBadge() {

        if (TVnotificationBadge != null) {
            if (newnotification == 0) {
                TVnotificationBadge.setVisibility(View.INVISIBLE);
            } else {
                TVnotificationBadge.setVisibility(View.VISIBLE);
                TVnotificationBadge.setText(String.valueOf(newnotification));

            }
        }
    }



    private void init() {
        StorageReference profileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile_picture/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                setProfilePicture(uri);
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
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
//        notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Home.this, Notification.class));
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        usingFirebaseDatabase();
    }

    private void usingFirebaseDatabase() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }
                usingFirebaseImages(mUploads);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void usingFirebaseImages(List<Upload> mUploads) {
        for (int i = 0; i < mUploads.size(); i++) {
            String downloadImageUrl = mUploads.get(i).getImageUrl();
            flipImages(downloadImageUrl);
        }
    }

    private void flipImages(String imageUrl) {
        ImageView imageView = new ImageView(this);
        Picasso.get().load(imageUrl).into(imageView);
        viewFlipper.addView(imageView);
        viewFlipper.startFlipping();
        viewFlipper.setAutoStart(true);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(in);
        viewFlipper.setFlipInterval(2000);
    }

    private void setProfilePicture(Uri uri) {
        Picasso.get().load(uri).into(profilePicture);
    }

    private void initWidgets() {
        viewFlipper = findViewById(R.id.viewflipper);
        profilePicture = findViewById(R.id.imageView);
        userNameView = findViewById(R.id.textView5);

        attendance = (CardView) findViewById(R.id.attendance);
        fees = (CardView) findViewById(R.id.fees);
        sick = (CardView) findViewById(R.id.sick);
        menu = (CardView) findViewById(R.id.menu);
        messout = (CardView) findViewById(R.id.messout);
    }

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