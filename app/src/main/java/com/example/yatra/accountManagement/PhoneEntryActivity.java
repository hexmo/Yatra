package com.example.yatra.accountManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.yatra.R;
import com.example.yatra.accountManagement.OtpVerificationActivity;
import com.google.android.material.textfield.TextInputLayout;

public class PhoneEntryActivity extends AppCompatActivity {
    //Variables and Objects declaration
    TextInputLayout loginPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);

        /* Hooks */
        loginPhone = findViewById(R.id.enter_phone);
    }

    public void launchOTPVerification(View view) {
        if(validatePhone()){
            Intent intent = new Intent(getApplicationContext(), OtpVerificationActivity.class);
            intent.putExtra("phoneNo", loginPhone.getEditText().getText().toString());
            startActivity(intent);
            finish();
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


}