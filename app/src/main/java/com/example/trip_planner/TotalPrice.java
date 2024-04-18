package com.example.trip_planner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TotalPrice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);

        // Get the total price passed from TripPlanner activity
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);

        // Retrieve other data from SaveList
        SaveList saveList = new SaveList(this);
        String fromDate = saveList.loadFromDate();
        String toDate = saveList.loadToDate();
        String transportationMode = saveList.loadSelectedTransportation();
        int numberOfPeople = saveList.loadNumberOfPeople(); // Update here

        // Find the TextViews for each data and set their values
        TextView textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        textViewTotalPrice.setText(String.format("Total Price: $%d", totalPrice));

        TextView textViewNumberOfPeople = findViewById(R.id.textViewNumberOfPeople);
        textViewNumberOfPeople.setText("Number of People: " + numberOfPeople);

        TextView textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewFromDate.setText("From Date: " + fromDate);

        TextView textViewToDate = findViewById(R.id.textViewToDate);
        textViewToDate.setText("To Date: " + toDate);

        TextView textViewTransportationMode = findViewById(R.id.textViewTransportationMode);
        textViewTransportationMode.setText("Transportation Mode: " + transportationMode);
    }
}
