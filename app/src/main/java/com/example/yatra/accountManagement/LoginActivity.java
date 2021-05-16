package com.example.yatra.accountManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.yatra.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //For firebase
    private FirebaseAuth mAuth;

    //Variables and Objects declaration
    TextInputLayout loginPhone, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hooks
        loginPhone = findViewById(R.id.login_phone);
        loginPassword = findViewById(R.id.login_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String userPhone = loginPhone.getEditText().getText().toString().trim();
        String userPassword = loginPassword.getEditText().getText().toString();

        if(validatePassword() && validatePhone()){
            Toast.makeText(this, "Validation complete.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePhone() {
        String value = loginPhone.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            loginPhone.setError("Phone number cannot be empty.");
        }else if(value.length() < 10){
            loginPhone.setError("Phone number should be 10 characters long.");
        }else if(!value.substring(0,2).equals("98")){
            loginPhone.setError("Phone number should start with 98.");
        }
        else {
            loginPhone.setError(null);
            loginPhone.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    private boolean validatePassword() {
        String value = loginPassword.getEditText().getText().toString();
        if (value.isEmpty()) {
            loginPassword.setError("Password cannot be empty.");
        } else {
            loginPassword.setError(null);
            loginPassword.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    public void gotToSignUpActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}