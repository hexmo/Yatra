package com.example.yatra.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.yatra.MainActivity;
import com.example.yatra.R;
import com.example.yatra.SplashScreenActivity;
import com.example.yatra.adapters.SliderAdapter;
import com.example.yatra.models.SliderImages;
import com.example.yatra.ticket.SearchTicketsActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment2 extends Fragment {

    int currentPage = 0;
    Timer timer;
    int NUM_PAGES = 5;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 1500; // time in milliseconds between successive task executions.

    ViewPager2 viewPager2;
    AutoCompleteTextView fromLocation, toLocation;
    TextInputLayout fromLocationInput, toLocationInput, dateInput;
    Button button;

    public HomeFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        //hooks
        viewPager2 = view.findViewById(R.id.home_view_pager);
        fromLocation = view.findViewById(R.id.from_location_input);
        toLocation = view.findViewById(R.id.to_location_input);
        button = view.findViewById(R.id.button_find_tickets);
        fromLocationInput = view.findViewById(R.id.from_location);
        ;
        toLocationInput = view.findViewById(R.id.to_location);
        ;
        dateInput = view.findViewById(R.id.datePicker);
        ;


        List<SliderImages> sliderImagesList = new ArrayList<>();
        sliderImagesList.add(new SliderImages(R.drawable.bus0));
        sliderImagesList.add(new SliderImages(R.drawable.bus1));
        sliderImagesList.add(new SliderImages(R.drawable.bus2));
        sliderImagesList.add(new SliderImages(R.drawable.bus3));
        sliderImagesList.add(new SliderImages(R.drawable.bus4));
        //set adapter
        viewPager2.setAdapter(new SliderAdapter(sliderImagesList, viewPager2));

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                viewPager2.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        //For drop down menu
        String[] cityArray = new String[]{"Kathmandu", "Pokhara", "Nepalgunj", "Biratnagar", "Jhapa", "Chitwan", "Lumbini", "Butwal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.gender_dropdown, cityArray);
        fromLocation.setAdapter(adapter);
        toLocation.setAdapter(adapter);


        //adding listener to button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLocation() && validateDate()) {
                    //Toast.makeText(view.getContext(), "Successful validation", Toast.LENGTH_SHORT).show();
                    String date = dateInput.getEditText().getText().toString();
                    String fromLocation = fromLocationInput.getEditText().getText().toString();
                    String toLocation = toLocationInput.getEditText().getText().toString();

                    //Open search results activity.



                    Intent searchResults = new Intent(view.getContext(), SearchTicketsActivity.class);
                    searchResults.putExtra("date", date);
                    searchResults.putExtra("fromLocation", fromLocation);
                    searchResults.putExtra("toLocationInput", toLocation);
                    startActivity(searchResults);
                }
            }
        });


        return view;
    }

    private boolean validateDate() {
        String date = dateInput.getEditText().getText().toString();
        if(date.isEmpty()){
            dateInput.setError("Please select date.");
        }else{
            dateInput.setError(null);
            dateInput.setErrorEnabled(false);
            return true;
        }

        return false;
    }

    private boolean validateLocation() {
        String fromLocation = fromLocationInput.getEditText().getText().toString();
        String toLocation = toLocationInput.getEditText().getText().toString();

        if (fromLocation.isEmpty()) {
            fromLocationInput.setError("From location can not be empty.");
        }else if (toLocation.isEmpty()) {
            toLocationInput.setError("To location can not be empty.");
        }else if(fromLocation.equals(toLocation)){
            fromLocationInput.setError("Both Location can not be same.");
            toLocationInput.setError("Both Location can not be same.");
        }else{
            fromLocationInput.setError(null);
            fromLocationInput.setErrorEnabled(false);
            toLocationInput.setError(null);
            toLocationInput.setErrorEnabled(false);
            return true;
        }

        return false;

    }


}