package com.example.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FAQ extends AppCompatActivity {
    String[] question = {"How to reset your password ?",
            "How to edit your profile pic ?",
            "About changing email ",
            " How to get hostel registration id ?",
            " How to get hostel registration id ?",
                            "What payment methods are accepted?",
            "Why can’t the app upload my profile pic"};
    ListView faqlistview;
    String[] answers = {"Open HostelApp > tap your profile pic on the top right corner, \nClick on change password \nReset your password their",
            "Open HostelApp > tap profile icon on top right corner > tap on the camera icon of your profile photo > tap Gallery to choose an existing photo or Camera to take a new photo",
            "Open HostelApp > tap profile icon on top right corner \nClick on the edit icon of mobile number and edit your mobile no" ,
            "Go to your hostel office and produce your documents to get registration id",
            "HostelApp contains all th", "Hostel App accepts all the payments including \nCredit card or debit card payments \nGpay \nPhone Pay  \nAmazone Pay\nPaytm ",
            "Your app should be able to upload files . Try close the app and Open again \nIf you are still having any troubles, please contact us and our development team will look into the problem for you."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Frequently Asked Questions");
        faqlistview = findViewById(R.id.listview);
        ArrayList<HashMap<String, Object>> listitems = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.faq_listview, new String[]{"FirstLine","SecondLine"},new int[]{R.id.question,R.id.answer});


        for (int i = 0; i < question.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("FirstLine", question[i]);
            map.put("SecondLine", answers[i]);
            listitems.add(map);
        }
        faqlistview.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    private void prepareListData() {
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new ArrayList<String>();
//        // listDataChild = new HashMap<String, List<String>>();
//
//        // Adding child data
//        listDataHeader.add("How to reset your password ?");
//        listDataHeader.add("How to edit your profile pic ?");
//        listDataHeader.add("About changing email ");
//        listDataHeader.add(" How to get hostel registration id ?");
//        listDataHeader.add("What content will my app have ?");
//        listDataHeader.add("What payment methods are accepted?");
//        listDataHeader.add("Why can’t the app upload my profile pic");
//        // Adding child data
//
//        listDataChild.add("Open HostelApp > tap your profile pic on the top right corner, \nClick on change password \nReset your password their");
//        listDataChild.add( "Open HostelApp > tap profile icon on top right corner > tap on the camera icon of your profile photo > tap Gallery to choose an existing photo or Camera to take a new photo");
//        listDataChild.add( "Open HostelApp > tap profile icon on top right corner \nClick on the edit icon of mobile number and edit your mobile no" );
//        listDataChild.add("Go to your hostel office and produce your documents to get registration id");
//        listDataChild.add("HostelApp contains all th");
//        listDataChild.add("Hostel App accepts all the payments including \nCredit card or debit card payments \nGpay \nPhone Pay  \nAmazone Pay\nPaytm ");
//        listDataChild.add("Your app should be able to upload files . Try close the app and Open again \nIf you are still having any troubles, please contact us and our development team will look into the problem for you.");
//
//        map.put(listDataHeader,listDataChild);
//        //   listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//
//    }
}