package com.example.canteenapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.UFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class TakeAwayBooking extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collection_db = db.collection("collection_timeslots");
    private LinearLayout bookingsLayout;

    private ArrayList<String> availableTimes = new ArrayList<>();

    ///button list
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
    private String studentNum;
    boolean alreadyBooked = false;
    public static final int MAXIMUM_CAPACITY = 20;

    final String TAG = "TakeAway_Booking";

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_away_booking);

        tenButton = findViewById(R.id.takeAway_ten_button);
        tenThirtyButton = findViewById(R.id.takeAway_ten_thirty_button);
        elevenButton = findViewById(R.id.takeAway_eleven_button);
        elevenThirtyButton = findViewById(R.id.takeAway_eleven_thity_button);
        twelveButton = findViewById(R.id.takeAway_twelve_button);
        twelveThirtyButton = findViewById(R.id.takeAway_twelve_thirty_button);
        oneButton = findViewById(R.id.takeAway_one_button);
        oneThirtyButton = findViewById(R.id.takeAway_one_thirty_button);
        twoButton =findViewById(R.id.takeAway_two_button);
        twoThirtyButton = findViewById(R.id.takeAway_two_thity_button);
        threeButton = findViewById(R.id.takeAway_three_button);
        threeThirtyButton = findViewById(R.id.takeAway_three_thirty_button);
        fourButton = findViewById(R.id.takeAway_four_button);

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

        bookingsLayout = findViewById(R.id.takeAway_booking_linear_layout);
        //get student num
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        studentNum = sharedPreferences.getString("studentNum", "NothingFound");
        ///get current time/date
        LocalTime timeNow = LocalTime.now();
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        //get available slots and highlight buttons
        getAvailableTimes(timeNow);
    }
    private void getAvailableTimes(LocalTime timeNow){
        collection_db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //clear old bookings
                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                    CollectionReference timeslot_collection = db.collection("collection_timeslots").document(snapshots.getId()).collection("bookings");
                    LocalTime timeSlotFromDb = LocalTime.parse(snapshots.getId());
                    timeslot_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot booking : queryDocumentSnapshots){
                                String stringDate = (String) booking.getData().get("date");
                                String bookedStudent = (String) booking.getData().get("student_num");
                                if (bookedStudent !=null && bookedStudent.equals(studentNum)){
                                    alreadyBooked = true;
                                    Intent alreadyBooked = new Intent(TakeAwayBooking.this, Reminder.class);
                                    alreadyBooked.putExtra("time", timeSlotFromDb.toString());
                                    startActivity(alreadyBooked);
                                }
                                if (!stringDate.equals("")){
                                    if (LocalDate.parse(stringDate).isBefore(LocalDate.now())){
                                        String id = booking.getId();
                                        timeslot_collection.document(id).delete();
                                    }
                                }
                            }
                        }
                    });
                    // get slots after time now and check for previous bookings
                    if (timeNow.isBefore(timeSlotFromDb)) {
                        timeslot_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int count = 0;
                                boolean spaceAvailable = true;
                                for (QueryDocumentSnapshot bookingSnapshot : queryDocumentSnapshots) {
                                    if (bookingSnapshot.getData().get("student_num") != null) {
                                        count++;
                                    }
                                    if (count >= MAXIMUM_CAPACITY) {
                                        spaceAvailable = false;
                                    }
                                }
                                if (spaceAvailable){
                                    for (Button button : buttons) {
                                        if (timeSlotFromDb.toString().equals(button.getText().toString())) {
                                            button.setTextColor(Color.GRAY);
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CollectionReference bookings_ref = db.collection("collection_timeslots").document(timeSlotFromDb.toString()).collection("bookings");
                                                    HashMap<String, Object> newBooking = new HashMap<>();
                                                    String bookingDate = LocalDate.now().toString();
                                                    newBooking.put("date", bookingDate);
                                                    newBooking.put("student_num", studentNum);
                                                    bookings_ref.add(newBooking);
                                                    Intent booking = new Intent(TakeAwayBooking.this, ConfirmationScreen.class);
                                                    booking.putExtra("time", button.getText());
                                                    startActivity(booking);
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
