package com.example.yatra.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.R;
import com.example.yatra.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {
    TextView userName, phoneNum;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    UserModel userModel;
    Button logoutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Hooks
        userName = view.findViewById(R.id.profileName);
        phoneNum = view.findViewById(R.id.profilePhone);
        logoutButton = view.findViewById(R.id.button_logout);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //populate data
        DocumentReference docRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                //populate text views
                userName.setText(userModel.getFirstName() + " " + userModel.getLastName());
                phoneNum.setText(mAuth.getCurrentUser().getPhoneNumber());
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(view.getContext(), "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });


        return view;
    }

}