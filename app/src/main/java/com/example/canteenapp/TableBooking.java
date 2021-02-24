package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TableBooking extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eatIn_db = db.collection("table_timeslots");

    final String TAG = "Table_Booking";
    ///still working on this
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking);
        ///get current time/date
        Date currentTime = Calendar.getInstance().getTime();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(currentTime);

        eatIn_db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                    Log.d(TAG, "onSuccess: " + snapshots.getData());
                }

            }
    });





        //check which slots are free in next two hours
        ///////iterate through slots and check number of students
        ///offer slots for next two hours that are free
        //update db
    }
}