package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.yatra.accountManagement.SignUpActivity;
import com.example.yatra.fragments.HistoryFragment;
import com.example.yatra.fragments.HomeFragment2;
import com.example.yatra.fragments.NotificationFragment;
import com.example.yatra.fragments.ProfileFragment;
import com.example.yatra.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_layout, new HomeFragment2()).commit();
    }

    //Switch fragment according to selected navigation menu
    final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {

                case R.id.homeFragment:
                    selectedFragment = new HomeFragment2();
                    break;

                case R.id.historyFragment:
                    selectedFragment = new HistoryFragment();
                    break;


                case R.id.notificationFragment:
                    selectedFragment = new NotificationFragment();
                    break;

                case R.id.profileFragment:
                    selectedFragment = new ProfileFragment();
                    break;

            }
            //display fragment code

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_layout, selectedFragment).commit();
            return true;
        }
    };


    //for Date picker
    public void launchDatePicker(View view) {
        //https://www.geeksforgeeks.org/material-design-date-picker-in-android/
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("Select departure date");

        //https://stackoverflow.com/questions/64627494/add-30-days-in-future-from-current-epoch-time-java
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, 1);
        long start_date = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 14);
        long end_date = calendar.getTimeInMillis();

        System.out.println("start_date :" + start_date);
        System.out.println("end_date :" + end_date);

        //constrain the calender between tomorrow and 15 days from now.
        // Build constraints.
        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder().setEnd(end_date)
                .setValidator(DateValidatorPointForward.from(start_date));


        materialDateBuilder.setCalendarConstraints(constraints.build());

//        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.from(start_date);
//        materialDateBuilder.setValidator(dateValidator);


        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        TextInputEditText textInputEditText = view.findViewById(R.id.date_picker_input);
//                        textInputEditText.setText(materialDatePicker.getHeaderText());

//                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                        String formattedDate = formatter.format(materialDatePicker.getHeaderText());
//                        textInputEditText.setText(formattedDate);

                        //https://www.tutorialguruji.com/android/how-to-change-the-format-of-material-date-picker-in-yyy-mm-dd/
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendar.setTimeInMillis(selection);
                        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
                        String formattedDate  = format.format(calendar.getTime());
                        textInputEditText.setText(formattedDate);
                    }
                });
    }

    private void checkIfAlreadyHasAccount() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);

                if (userModel == null){
                    startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    finish();
                }
            }
        });

    }

}