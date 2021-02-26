package com.example.canteenapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;

public class TableBooking extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eatIn_db = db.collection("eatIn_timeslots");

    final String TAG = "Table_Booking";
    ///still working on this
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking);

        ///get current time/date
        LocalTime timeNow = LocalTime.now();
        eatIn_db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                    LocalTime timeSlotFromDb = LocalTime.parse(snapshots.getId());
                    // get slots after time now
                    if (timeNow.isBefore(timeSlotFromDb)){
                        Log.d(TAG, " time " + timeSlotFromDb);
                        CollectionReference timeslot_collection = db.collection("eatIn_timeslots").document(snapshots.getId()).collection("bookings");
                        timeslot_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot bookingSnapshot : queryDocumentSnapshots){
                                    Log.d(TAG, "booking" + bookingSnapshot.getData().get("student_num"));
                                    Log.d(TAG, "booking" + bookingSnapshot.getData().get("date"));

                                }
                            }
                        });
                        // check if space

                    }
                }

            }
    });





        //check which slots are free in next two hours
        ///////iterate through slots and check number of students
        ///offer slots for next two hours that are free
        //update db
    }
}