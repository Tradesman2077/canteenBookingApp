package com.example.canteenapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.UFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TableBooking extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eatIn_db = db.collection("eatIn_timeslots");
    private LinearLayout bookingsLayout;

    private ArrayList<String> availableTimes = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();

    private Button tenButton;
    private Button tenThirtyButton;
    private Button elevenButton;
    private Button elevenThirtyButton;
    private Button twelveButton;
    private Button twelveThirtyButton;
    private Button oneButton;
    private Button oneThirtyButton;
    private Button twoButton;
    private Button twoThirtyButton;
    private Button threeButton;
    private Button threeThirtyButton;
    private Button fourButton;

    final String TAG = "Table_Booking";

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking);

        tenButton = findViewById(R.id.ten_button);
        tenThirtyButton = findViewById(R.id.ten_thirty_button);
        elevenButton = findViewById(R.id.eleven_button);
        elevenThirtyButton = findViewById(R.id.eleven_thity_button);
        twelveButton = findViewById(R.id.twelve_button);
        twelveThirtyButton = findViewById(R.id.twelve_thirty_button);
        oneButton = findViewById(R.id.one_button);
        oneThirtyButton = findViewById(R.id.one_thirty_button);
        twoButton =findViewById(R.id.two_button);
        twoThirtyButton = findViewById(R.id.two_thity_button);
        threeButton = findViewById(R.id.three_button);
        threeThirtyButton = findViewById(R.id.three_thirty_button);
        fourButton = findViewById(R.id.four_button);

        buttons.add(tenButton);
        buttons.add(tenThirtyButton);
        buttons.add(elevenButton);
        buttons.add(elevenThirtyButton);
        buttons.add(twelveButton);
        buttons.add(twelveThirtyButton);
        buttons.add(oneButton);
        buttons.add(oneThirtyButton);
        buttons.add(twoButton);
        buttons.add(twoThirtyButton);
        buttons.add(threeButton);
        buttons.add(threeThirtyButton);
        buttons.add(fourButton);

        bookingsLayout = findViewById(R.id.booking_linear_layout);

        ///get current time/date
        LocalTime timeNow = LocalTime.now();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        //get available slots and highlight buttons
        getAvailableTimes(timeNow);
    }


    private void getAvailableTimes(LocalTime timeNow){

        eatIn_db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                //clear old bookings
                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                    CollectionReference timeslot_collection = db.collection("eatIn_timeslots").document(snapshots.getId()).collection("bookings");
                    LocalTime timeSlotFromDb = LocalTime.parse(snapshots.getId());
                    timeslot_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot booking : queryDocumentSnapshots){
                                String stringDate = (String) booking.getData().get("date");
                                Log.d(TAG, "onSuccess: + " + stringDate);
                                if (!stringDate.equals("")){
                                    if (LocalDate.parse(stringDate).isBefore(LocalDate.now())){
                                        String id = booking.getId();
                                        timeslot_collection.document(id).delete();
                                    }
                                }
                            }
                        }
                    });
                    // get slots after time now
                    if (timeNow.isBefore(timeSlotFromDb)) {
                        Log.d(TAG, timeSlotFromDb.toString());
                        timeslot_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int count = 0;
                                boolean spaceAvailable = true;
                                for (QueryDocumentSnapshot bookingSnapshot : queryDocumentSnapshots) {
                                    if (bookingSnapshot.getData().get("student_num") != null) {
                                        count++;
                                    }
                                    if (count >= 3) {
                                        spaceAvailable = false;
                                    }
                                }
                                if (spaceAvailable){
                                    for (Button button : buttons){
                                        if (timeSlotFromDb.toString().equals(button.getText().toString())){

                                            button.setTextColor(Color.GRAY);
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Log.d(TAG, "onClick: clicked");
                                                    CollectionReference bookings_ref = db.collection("eatIn_timeslots").document(timeSlotFromDb.toString()).collection("bookings");
                                                    HashMap<String, Object> newBooking = new HashMap<>();
                                                    String bookingDate = LocalDate.now().toString();
                                                    newBooking.put("date", bookingDate);
                                                    newBooking.put("student_num", "234234234");
                                                    bookings_ref.add(newBooking);
                                                    startActivity(new Intent(TableBooking.this, ConfirmationScreen.class));
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
