package com.example.hostelapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;

public class MyFirebaseInstanceIDService extends Service {
    private static final String TAG = "MyFirebaseInstanceIDService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
