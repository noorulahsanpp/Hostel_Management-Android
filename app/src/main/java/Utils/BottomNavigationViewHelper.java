package Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.hostelapp.Home;
import com.example.hostelapp.Login;
import com.example.hostelapp.Notification;
import com.example.hostelapp.Profile;
import com.example.hostelapp.R;
import com.example.hostelapp.Sick;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";
    public static void setupBottomNavigationView(BottomNavigationView bottomNavigationView)
    {
        Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");

    }

   public static void enableNavigation(final Context context , BottomNavigationView view){

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                 /*  case R.id.ic_home:
                       Intent intent1 = new Intent(context, Home.class); //activity_num 0
                      context.startActivity(intent1);
                      break;*/
                /*   case R.id.ic_notification:
                        Intent intent2 = new Intent(context, Notification.class);//activity_num 1
                        context.startActivity(intent2);
                        break;*/
                   /* case R.id.ic_profile:
                      Intent intent3 = new Intent(context, Profile.class);//activity_num 2
                      context.startActivity(intent3);
                       break;*/
                }
                return false;
            }
        });
   }

}
