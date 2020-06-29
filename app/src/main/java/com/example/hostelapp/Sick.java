package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import Utils.BottomNavigationViewHelper;

public class Sick extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
   private Spinner spinner;
    private Button button14;
    private RadioButton breakfast, lunch, dinner;
    private RadioGroup radiogroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sick);

        spinner = (Spinner)findViewById(R.id.spinner);

        button14=(Button)findViewById(R.id.button14);
        radiogroup=(RadioGroup)findViewById(R.id.groupradio);
        radiogroup.clearCheck(); // reset radio buttons
        breakfast=(RadioButton)findViewById(R.id.radioButton);
        lunch=(RadioButton)findViewById(R.id.radioButton1);
        dinner=(RadioButton)findViewById(R.id.radioButton2);
       // setupBottomNavigationView();

        breakfast.setEnabled(false);
       lunch.setEnabled(false);
        dinner.setEnabled(false);



        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Full Day");
        categories.add("Custom");

         ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sick.this,Home.class));
            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(getBaseContext(),item+" selected",Toast.LENGTH_LONG).show();

    if (item == "Custom") {
        breakfast.setEnabled(true);
        lunch.setEnabled(true);
        dinner.setEnabled(true);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId = radiogroup.getCheckedRadioButtonId();
                RadioButton option = (RadioButton) radiogroup.findViewById(selectedId);

                Toast.makeText(Sick.this, option.getText(), Toast.LENGTH_SHORT).show();
            }


        });
    }
        else
        {

            breakfast.setEnabled(false);
            lunch.setEnabled(false);
            dinner.setEnabled(false);
        }
      



    
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


}
