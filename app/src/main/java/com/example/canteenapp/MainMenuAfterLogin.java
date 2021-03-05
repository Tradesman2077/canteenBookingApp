package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuAfterLogin extends AppCompatActivity {

    private Button bookTableButton;
    private Button takeAwayButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_after_login);

        bookTableButton = findViewById(R.id.book_button);
        takeAwayButton = findViewById(R.id.collect_button);




        bookTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookTableIntent = new Intent(MainMenuAfterLogin.this, TableBooking.class);
                startActivity(bookTableIntent);
            }
        });

        takeAwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeAwayIntent = new Intent(MainMenuAfterLogin.this, TakeAwayBooking.class);
                startActivity(takeAwayIntent);
            }
        });


    }
}