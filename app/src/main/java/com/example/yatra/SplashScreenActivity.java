package com.example.yatra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.yatra.accountManagement.PhoneEntryActivity;
import com.example.yatra.accountManagement.SignUpActivity;
import com.example.yatra.models.UserModel;
import com.example.yatra.notification.BroadcastReceiverClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = new Intent(getApplicationContext(), BroadcastReceiverClass.class);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 68, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long triggerTime = 2 * 60 * 1000; //set time here to run the notifications.

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, triggerTime, sender);
        }


        // https://stackoverflow.com/questions/4743116/get-screen-width-and-height-in-android
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

//        Log.d("Slider height: ", String.valueOf(deviceWidth) + " splash pixels");
        // This method will be executed once the timer is over
        sharedPref = getSharedPreferences("app_preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("device_width", deviceWidth);
        editor.apply();


        Intent openSignUp = new Intent(SplashScreenActivity.this, SignUpActivity.class);
        Intent openPhoneEntry = new Intent(SplashScreenActivity.this, PhoneEntryActivity.class);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (mAuth.getCurrentUser() != null) {
                    checkIfAlreadyHasAccount();


                } else {
                    startActivity(openPhoneEntry);
                }
                finish();
            }
        }, 1000);


    }

    private void checkIfAlreadyHasAccount() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);

                if (userModel == null){
                    startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
                    finish();
                }else{
                    Intent openHome = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(openHome);
                }
            }
        });

    }
}