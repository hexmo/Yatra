package com.example.yatra.ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.R;
import com.example.yatra.models.BusModel;
import com.example.yatra.utils.LoadingClass;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchTicketsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    LoadingClass loadingClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tickets);
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String fromLocation = intent.getStringExtra("fromLocation");
        String toLocationInput = intent.getStringExtra("toLocationInput");

        //hooks
        recyclerView = findViewById(R.id.search_results_recycler_view);

        //Read data from fire store and load data.
        firebaseFirestore = FirebaseFirestore.getInstance();

        //https://youtu.be/cBwaJYocb9I
        //Query
        Query query = firebaseFirestore.collection("bus")
                .whereEqualTo("journeyDate",date)
                .whereEqualTo("startDestination",fromLocation)
                .whereEqualTo("endDestination",toLocationInput);

        //Recycler options
        FirestoreRecyclerOptions<BusModel> options = new FirestoreRecyclerOptions.Builder<BusModel>()
                .setQuery(query, BusModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BusModel, BusViewHolder>(options) {
            @NonNull
            @Override
            public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bus_search_results_layout,parent,false);
                return new BusViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BusViewHolder holder, int position, @NonNull BusModel model) {
                holder.companyName.setText(model.getCompanyName());
                holder.time.setText("Time: " + model.getDepartureTime());
                holder.seatsAvailable.setText("Seats available: " + model.getAvailableSeatCount());
                holder.ticketPrice.setText("Rs. " + model.getPrice());

                holder.ticketView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =  new Intent(view.getContext(), ViewBusFullDetailActivity.class);
                        intent.putExtra("busModel",model);
                        view.getContext().startActivity(intent);
                    }
                });
            }
        };

//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    public void goBack(View view) {
        finish();
    }

    public void viewBusDetails(View view) {

    }

    private class BusViewHolder extends RecyclerView.ViewHolder{

        private TextView companyName, time, seatsAvailable, ticketPrice;
        private ConstraintLayout ticketView;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.yatayat_name);
            time = itemView.findViewById(R.id.departure_time);
            seatsAvailable = itemView.findViewById(R.id.seats_available);
            ticketPrice = itemView.findViewById(R.id.ticket_price);
            ticketView = itemView.findViewById(R.id.bus_search_result_layout);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}