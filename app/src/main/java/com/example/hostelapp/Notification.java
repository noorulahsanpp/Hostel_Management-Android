package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Utils.BottomNavigationViewHelper;
import Utils.FirebaseMethods;

public class Notification extends AppCompatActivity {
    private static final String TAG = "Notification";
    private static final int ACTIVITY_NUM = 1;
    public static final String MyPREFERENCES = "MyPrefs" ;
   private ListView notification;
    private String hostel = "";

   private String topic;
    private Timestamp timestamp;
   private String description;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseMethods firebaseMethods;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notification);
     //   setupBottomNavigationView();

        mContext = Notification.this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        hostel = sharedPreferences.getString("hostel", "");
        notification = (ListView)findViewById(R.id.listview);
        mnotification();
           }

    private void mnotification() {

        final List<HashMap<String, String>> listitems = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.list_items, new String[]{"First Line","Second Line","date"},new int[]{R.id.topic,R.id.description,R.id.date});
        collectionReference = firebaseFirestore.collection("inmates").document(hostel).collection("notification");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        topic = document.get("topic").toString();
                        description = document.get("description").toString();
                        timestamp = document.getTimestamp("date");
                        Date date = timestamp.toDate();
                        String date1 = simpleDateFormat.format(date);
                        Map<Object, Object> topicdescription = new HashMap<>();
                        topicdescription.put(topic, description);
                        Iterator it = topicdescription.entrySet().iterator();
                        HashMap<String, String> resultMap = new HashMap<>();
                        Map.Entry pair = (Map.Entry) it.next();
                        resultMap.put("First Line", pair.getKey().toString());
                        resultMap.put("Second Line",pair.getValue().toString());
                        resultMap.put("date", date1);
                        listitems.add(resultMap);

                        notification.setAdapter(adapter);

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }

        });
    }


}
  /*  private void setupBottomNavigationView() {
        // Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(Notification.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }*/





