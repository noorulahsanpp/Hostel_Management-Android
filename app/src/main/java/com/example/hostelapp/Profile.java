package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import Utils.BottomNavigationViewHelper;

public class Profile extends AppCompatActivity {
    private static final String TAG = "Profile";
    private static final int ACTIVITY_NUM = 2;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView profilePicture,edit;
    private TextView  emailTv, phoneTv;
    private TextView name,admno;
    private Button  editProfile;
    private LinearLayout changePassword,logout;
    private String userID, hostel, admissionNumber;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
     //   setupBottomNavigationView();

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        // get access to the root folder of the storage
        storageReference = FirebaseStorage.getInstance().getReference();

       Intent intent = getIntent();
        hostel = intent.getStringExtra("hostel");
        admissionNumber = intent.getStringExtra("admission_number");

        StorageReference profileRef = storageReference.child("users/"+ mAuth.getCurrentUser().getUid()+"/profile_picture/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                setProfilePicture(uri);
            }
        });

        profilePicture = findViewById(R.id.imageView);
        edit = findViewById(R.id.edit);
        name = findViewById(R.id.name);
        admno = findViewById(R.id.admno);
        emailTv = findViewById(R.id.email);
        phoneTv = findViewById(R.id.phone);
        changePassword = findViewById(R.id.linear3);
        logout = findViewById(R.id.linear2);
       // editProfile = findViewById(R.id.button13);
       // admno.setText(admissionNumber);
       DocumentReference documentReference = firebaseFirestore.collection("registered").document("MH001");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                name.setText(documentSnapshot.getString("name"));
                admno.setText(documentSnapshot.getString("admission_number"));
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(Profile.this, Login.class));
            }
        });

            String email = user.getEmail();
            String phone = user.getPhoneNumber().toString();


            emailTv.setText(email);
            phoneTv.setText(phone);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), changepassword.class));
            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000 );
            }
        });
    }


  /*  private void setupBottomNavigationView(){
        Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(Profile.this,bottomNavigationView);
       android.view.Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem( ACTIVITY_NUM );
        menuItem.setChecked(true);

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri)
    {
        final StorageReference fileReference = storageReference.child("users/"+ mAuth.getCurrentUser().getUid()+"/profile_picture/profile.jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setProfilePicture(uri);
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Try again", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void setProfilePicture(Uri uri)
    {
        Picasso.get().load(uri).into(profilePicture);
    }

}

