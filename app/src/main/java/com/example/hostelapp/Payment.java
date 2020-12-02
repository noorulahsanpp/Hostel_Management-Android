package com.example.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Payment extends AppCompatActivity {

    public static final String GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    Button payBtn;
    EditText nameEt, amountEt, noteEt, upiidEt;
    Uri uri;
    public static String name, upiid, note, amount, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
        payBtn = findViewById(R.id.button2);
        nameEt = findViewById(R.id.ed1);
        upiidEt = findViewById(R.id.ed2);
        amountEt = findViewById(R.id.ed3);
        noteEt = findViewById(R.id.ed4);
        
        payBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString().trim();
                upiid = upiidEt.getText().toString().trim();
                amount = amountEt.getText().toString().trim();
                note = noteEt.getText().toString().trim();

                uri = getUpiPaymentUri(name, upiid, note, amount);
                payWithGpay(GPAY_PACKAGE_NAME);
            }
        });

    }
    private static Uri getUpiPaymentUri(String name, String upiId, String note, String amount){
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithGpay(String packageName){

        if(isAppInstalled(this, packageName)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            startActivityForResult(intent,0);
        }
        else{
            Toast.makeText(Payment.this, "Google Pay is not installed. Please install and try again later", Toast.LENGTH_LONG).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if(data != null){
            status = data.getStringExtra("status");
        }
        if((RESULT_OK == resultCode) && status.equals("success")){
            Toast.makeText(Payment.this, "Transaction Successful", Toast.LENGTH_LONG).show();
            noteEt.setText("Transaction successfull of amount "+ amount);
            noteEt.setTextColor(Color.GREEN);
        }
        else{
            Toast.makeText(Payment.this, "Transaction failed. Please try again later", Toast.LENGTH_LONG).show();
            noteEt.setText("Transaction Failed of amount "+ amount);
            noteEt.setTextColor(Color.RED);

            }
    }

    public static boolean isAppInstalled(Context context, String packageName){
        try{
                context.getPackageManager().getApplicationInfo(packageName, 0);
        }catch(PackageManager.NameNotFoundException e){
            return false;
        }
        return true;
    }
}