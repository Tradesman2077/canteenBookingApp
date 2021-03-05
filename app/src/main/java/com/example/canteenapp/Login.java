package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText studentNumInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        studentNumInput = findViewById(R.id.student_num_edit_text);
    }

    public void confirmButton(View view){
        Intent intent = new Intent(this, MainMenuAfterLogin.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("studentNum", studentNumInput.getText().toString().trim());
        editor.apply();
        startActivity(intent);
    }
}