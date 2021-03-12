package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationScreen extends AppCompatActivity {

    TextView bookingText;
    Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        bookingText = findViewById(R.id.booking_confimation_textEdit);
        closeButton = findViewById(R.id.close_button);

        String time = (String) getIntent().getExtras().get("time");

        bookingText.setText("Booking confirmed for - " + time);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmationScreen.this, MainActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConfirmationScreen.this, MainActivity.class));
    }
}