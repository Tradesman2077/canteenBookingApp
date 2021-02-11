package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void confirmButton(View view){
        Intent intent = new Intent(this, MainMenuAfterLogin.class);
        startActivity(intent);
    }
}