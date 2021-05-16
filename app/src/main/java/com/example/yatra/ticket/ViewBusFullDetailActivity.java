package com.example.yatra.ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.R;
import com.example.yatra.models.BookingModel;
import com.example.yatra.models.BusModel;
import com.example.yatra.utils.StringChunker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class ViewBusFullDetailActivity extends AppCompatActivity {
    TextView bookCompanyName, bookFromTo, bookDate, bookTime,
            bookFacilities, bookBusNumber, bookAvailableSeats,
            bookPrice;

    TextInputLayout fullName, contactNo, seats;
    int bookedSeatCount = 0;
    BusModel model;

    //Firebase
    FirebaseFirestore db;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_view_bus_full_detail);
        //hooks
        bookCompanyName = findViewById(R.id.bookCompanyName);
        bookFromTo = findViewById(R.id.bookFromTo);
        bookDate = findViewById(R.id.bookDate);
        bookTime = findViewById(R.id.bookTime);
        bookFacilities = findViewById(R.id.bookFacilities);
        bookBusNumber = findViewById(R.id.bookBusNumber);
        bookAvailableSeats = findViewById(R.id.bookAvailableSeats);
        bookPrice = findViewById(R.id.bookPrice);

        fullName = findViewById(R.id.bookPassengerName);
        contactNo = findViewById(R.id.bookPassengerContact);
        seats = findViewById(R.id.bookPassengerSeat);

        // Get object from intent
        Intent intent = getIntent();
        model = (BusModel) intent.getSerializableExtra("busModel");

        System.out.println(model.toString());


        //Populate text views
        bookFromTo.setText(model.getStartDestination() + " to " + model.getEndDestination());
        bookDate.setText(model.getJourneyDate());
        bookTime.setText(model.getDepartureTime());
        bookFacilities.setText(model.getFacilities());
        bookBusNumber.setText(model.getLisencePlate());
        bookAvailableSeats.setText(model.getAvailableSeats());
        bookPrice.setText("Rs. " + model.getPrice());
        bookCompanyName.setText(model.getCompanyName());


    }

    public void goBack(View view) {
        finish();
    }

    public void bookTickets(View view) {
        if (validateName() && validatePhone() && validateSeats()) {
            //Toast.makeText(this, "Yay! Booking Validation Complete", Toast.LENGTH_SHORT).show();
            createAndSaveTicket();

        }

    }

    private void createAndSaveTicket() {
        String bookerName = fullName.getEditText().getText().toString();
        String bookedSeats = seats.getEditText().getText().toString().trim().toUpperCase();
        String bookerContact = contactNo.getEditText().getText().toString().trim();


        DocumentReference newBookingRef = db.collection("booking").document();
        String bookingId = newBookingRef.getId();

        BookingModel bookingModel = new BookingModel(bookingId,bookerName,bookerContact,bookedSeats,model.getId(),mAuth.getCurrentUser().getUid(),model.getJourneyDate());
        newBookingRef.set(bookingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Now since we have booked tickets its time to update data.
                updateBus();
            }
        });


    }

    private void updateBus() {
        String bookedSeats = seats.getEditText().getText().toString().trim().toUpperCase();
        String[] selectedSeats = StringChunker.chunk(bookedSeats);
        String[] availableSeats = StringChunker.chunk(model.getAvailableSeats());

        model.setAvailableSeats(StringChunker.arrayDifference(availableSeats,selectedSeats));

        //https://firebase.google.com/docs/firestore/manage-data/add-data#update-data
        DocumentReference updateBusRef = db.collection("bus").document(model.getId());

        updateBusRef.set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ViewBusFullDetailActivity.this, "Successfully Booked Ticket", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }

    private boolean validateSeats() {
        String value = seats.getEditText().getText().toString().trim().toUpperCase();
        //https://stackoverflow.com/questions/16524709/finding-if-an-array-contains-all-elements-in-another-array
        String[] selectedSeats = StringChunker.chunk(value);
        String[] availableSeats = StringChunker.chunk(model.getAvailableSeats());
        System.out.println(selectedSeats);
        System.out.println(availableSeats);

        if (value.isEmpty()) {
            seats.setError("Please Select Seats");
        } else if (!Arrays.asList(availableSeats).containsAll(Arrays.asList(selectedSeats))) {
            seats.setError("Please select valid seats.");
            return false;
        } else {
            seats.setError(null);
            seats.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    private boolean validatePhone() {
        String value = contactNo.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            contactNo.setError("Phone number cannot be empty.");
        } else if (value.length() < 10) {
            contactNo.setError("Phone number should be 10 characters long.");
        } else if (!value.substring(0, 2).equals("98")) {
            contactNo.setError("Phone number should start with 98.");
        } else {
            contactNo.setError(null);
            contactNo.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    private boolean validateName() {
        String value = fullName.getEditText().getText().toString();
        if (value.isEmpty()) {
            fullName.setError("Full name can't be empty.");
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
        return false;
    }
}