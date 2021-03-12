package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Reminder extends AppCompatActivity {

    TextView textReminder;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        String time = (String) getIntent().getExtras().get("time");

        close = findViewById(R.id.close_button_reminder);
        textReminder = findViewById(R.id.reminder_text);

        textReminder.setText("You already have a booking at- " + time + " please wait till after this booking before booking again.");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Reminder.this, MainActivity.class));
            }
        });
    }
}