package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class Verify_Otp extends AppCompatActivity {

    EditText inputNumber1, inputNumber2, inputNumber3, inputNumber4, inputNumber5, inputNumber6;
    TextView codeSentTo, requestAgain;
    Button submit;
    String getOtpBackend;
    ImageView backBtn;
    OtpReceiver otpReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.verify_otp);

        backBtn = findViewById(R.id.backArrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Verify_Otp.this, Enter_Phone.class);
                startActivity(intent);
            }
        });

        inputNumber1 = findViewById(R.id.edt1);
        inputNumber2 = findViewById(R.id.edt2);
        inputNumber3 = findViewById(R.id.edt3);
        inputNumber4 = findViewById(R.id.edt4);
        inputNumber5 = findViewById(R.id.edt5);
        inputNumber6 = findViewById(R.id.edt6);
        codeSentTo = findViewById(R.id.codesendto);
        requestAgain = findViewById(R.id.requestotp);
        submit = findViewById(R.id.verifycode);

        //code send to that number
        codeSentTo.setText(String.format("+91%s", getIntent().getStringExtra("mobile")));

        //receive otp from backend
        getOtpBackend = getIntent().getStringExtra("backendOtp");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputNumber1.getText().toString().trim().isEmpty() && !inputNumber2.getText().toString().trim().isEmpty() && !inputNumber3.getText().toString().trim().isEmpty()) {
                    String enterCodeOtp = inputNumber1.getText().toString() + inputNumber2.getText().toString() +
                            inputNumber3.getText().toString() + inputNumber4.getText().toString() +
                            inputNumber5.getText().toString() + inputNumber6.getText().toString();

                    if(getOtpBackend != null){
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getOtpBackend, enterCodeOtp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //if task is successful
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(getApplicationContext(), Select_Profile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(Verify_Otp.this, "Enter correct otp", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(Verify_Otp.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(Verify_Otp.this, "Please enter otp", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Verify_Otp.this, "Please enter all number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberOtpMove();

        requestAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        Verify_Otp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Verify_Otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String newBackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getOtpBackend = newBackendOtp;
                                Toast.makeText(Verify_Otp.this, "reSend otp successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

        //method for auto otp receiver
        autoOtpReceiver();
    }

    private void autoOtpReceiver() {
        otpReceiver = new OtpReceiver();
        this.registerReceiver(otpReceiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        otpReceiver.initListener(new OtpReceiver.OtpReceiverListener() {
            @Override
            public void onOtpSuccess(String otp) {
                int o1 = Character.getNumericValue(otp.charAt(0));
                int o2 = Character.getNumericValue(otp.charAt(1));
                int o3 = Character.getNumericValue(otp.charAt(2));
                int o4 = Character.getNumericValue(otp.charAt(3));
                int o5 = Character.getNumericValue(otp.charAt(4));
                int o6 = Character.getNumericValue(otp.charAt(5));

                inputNumber1.setText(String.valueOf(o1));
                inputNumber2.setText(String.valueOf(o2));
                inputNumber3.setText(String.valueOf(o3));
                inputNumber4.setText(String.valueOf(o4));
                inputNumber5.setText(String.valueOf(o5));
                inputNumber6.setText(String.valueOf(o6));



                if(getOtpBackend !=null){
                    Intent intent = new Intent(Verify_Otp.this, Select_Profile.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onOtpTimeout() {
                Toast.makeText(Verify_Otp.this, "OTP TIME OUT / PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void numberOtpMove() {
        inputNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputNumber2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputNumber3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputNumber4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputNumber5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputNumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputNumber6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // method to unRegister otp after verification
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(otpReceiver != null){
            unregisterReceiver(otpReceiver);
        }
    }
}

