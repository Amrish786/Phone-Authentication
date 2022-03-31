package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class Enter_Phone extends AppCompatActivity{
    EditText enterPhone;
    Button sendOtp;
    String phone;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.enter_phone);

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Enter_Phone.this, Select_Language.class));
            }
        });

        enterPhone = findViewById(R.id.enterphone);
        sendOtp = findViewById(R.id.sendotp);
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = enterPhone.getText().toString().trim();
                if (!phone.isEmpty()) {
                    if (phone.length() == 10) {

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + phone,
                                60, TimeUnit.SECONDS, Enter_Phone.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(Enter_Phone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        Intent intent = new Intent(getApplicationContext(), Verify_Otp.class);
                                        intent.putExtra("mobile", phone);
                                        intent.putExtra("backendOtp", backendOtp);
                                        startActivity(intent);
                                    }
                                }
                        );

                    } else {
                        Toast.makeText(Enter_Phone.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Enter_Phone.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}