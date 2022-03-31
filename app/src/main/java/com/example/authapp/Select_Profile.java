package com.example.authapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Select_Profile extends AppCompatActivity {

    Button continueBtn, radioBtn1, radioBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.select_profile);

        continueBtn = findViewById(R.id.selectProfileBtn);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Select_Profile.this, "Continue Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}