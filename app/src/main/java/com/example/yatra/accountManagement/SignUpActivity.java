package com.example.yatra.accountManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.yatra.MainActivity;
import com.example.yatra.R;
import com.example.yatra.models.UserModel;
import com.example.yatra.utils.LoadingClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    //For firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    //Variables and Objects declaration
    TextInputLayout firstName, lastName, gender;
    AutoCompleteTextView autoCompleteTextView;
    FirebaseFirestore db;
    private LoadingClass loadingClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loadingClass = new LoadingClass(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //Hooks
        firstName = findViewById(R.id.save_first_name);
        lastName = findViewById(R.id.save_second_name);
        gender = findViewById(R.id.save_gender);
        autoCompleteTextView = findViewById(R.id.gender_auto_complete_text_view);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Reference : https://www.youtube.com/watch?v=d6lDVQ8aBRc
        String[] genderArray = new String[]{"Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this, R.layout.gender_dropdown, genderArray);
        autoCompleteTextView.setAdapter(adapter);
    }


    public void saveDetails(View view) {
        if (validateFirstName() && validateSecondName() && validateGender()) {
            UserModel userModel = new UserModel(mAuth.getCurrentUser().getUid(), firstName.getEditText().getText().toString(), lastName.getEditText().getText().toString(), gender.getEditText().getText().toString());

            loadingClass.startLoadingDialog();
            db.collection("user").document(mAuth.getCurrentUser().getUid()).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("User data:", "DocumentSnapshot successfully written!");
                    Toast.makeText(SignUpActivity.this, "Successfully saved your details.", Toast.LENGTH_SHORT).show();
                    loadingClass.startLoadingDialog();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("User data:", "Error writing document", e);
                    loadingClass.startLoadingDialog();
                    Toast.makeText(SignUpActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateFirstName() {
        String value = firstName.getEditText().getText().toString();
        if (value.isEmpty()) {
            firstName.setError("First name cannot be empty.");
        } else {
            firstName.setError(null);
            firstName.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    private boolean validateSecondName() {
        String value = lastName.getEditText().getText().toString();
        if (value.isEmpty()) {
            lastName.setError("Last name be empty.");
        } else {
            lastName.setError(null);
            lastName.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    private boolean validateGender() {
        String value = gender.getEditText().getText().toString();
        if (value.isEmpty()) {
            gender.setError("Please select gender.");
        } else {
            gender.setError(null);
            gender.setErrorEnabled(false);
            return true;
        }
        return false;
    }




}