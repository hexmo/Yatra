package com.example.yatra.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.R;
import com.example.yatra.models.BookingModel;
import com.example.yatra.models.BusModel;
import com.example.yatra.models.UserModel;
import com.example.yatra.ticket.SearchTicketsActivity;
import com.example.yatra.ticket.ViewBusFullDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore, db;

    BusModel busModel;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.my_tickets_recycler_view);

        //query
        Query query = firebaseFirestore.collection("booking")
                .whereEqualTo("bookerID", mAuth.getCurrentUser().getUid());

        //Recycler options
        FirestoreRecyclerOptions<BookingModel> options = new FirestoreRecyclerOptions.Builder<BookingModel>()
                .setQuery(query, BookingModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BookingModel, TicketViewHolder>(options) {

            @NonNull
            @Override
            public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_ticket_layout, parent, false);

                return new TicketViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TicketViewHolder holder, int position, @NonNull BookingModel model) {
                holder.seats.setText("Seats: " + model.getBookedSeats());
                holder.passengerName.setText(model.getBookerName());

                //getting bus details from fire store
                DocumentReference docRef = firebaseFirestore.collection("bus").document(model.getBusID());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        busModel = documentSnapshot.toObject(BusModel.class);
                        //populate text views
                        holder.route.setText(busModel.getStartDestination() + " to " + busModel.getEndDestination());
                        holder.time.setText(busModel.getDepartureTime());
                        holder.date.setText(busModel.getJourneyDate());
                        holder.company.setText(busModel.getCompanyName());
                        holder.licencePlate.setText(busModel.getLisencePlate());

                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //delete ticket
                                //first update the bus model
                                busModel.setAvailableSeats(busModel.getAvailableSeats() + " " + model.getBookedSeats());
                                DocumentReference updateBusRef = db.collection("bus")
                                        .document(busModel.getId());

                                updateBusRef.set(busModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        db.collection("booking")
                                                .document(model.getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "Successfully canceled tickets.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                    }
                                });

                            }
                        });

                    }
                });


            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    private class TicketViewHolder extends RecyclerView.ViewHolder {

        private TextView route, passengerName, seats, time, date, company, licencePlate;
        private Button button;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            route = itemView.findViewById(R.id.ticket_from_to);
            passengerName = itemView.findViewById(R.id.ticket_passenger_name);
            seats = itemView.findViewById(R.id.ticket_booked_seats);
            time = itemView.findViewById(R.id.ticket_time);
            date = itemView.findViewById(R.id.ticket_date);
            company = itemView.findViewById(R.id.ticket_company);
            licencePlate = itemView.findViewById(R.id.ticket_bus_number);
            button = itemView.findViewById(R.id.cancel_ticket_button);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}