package com.example.trip_planner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class TotalPrice extends AppCompatActivity {

    private SaveList mSaveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);

        // Initialize SaveList
        mSaveList = new SaveList(this);

        // Rating bar & Submission
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button submitRatingButton = findViewById(R.id.submitRatingButton);

        // TextViews for saving
        TextView textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        TextView textViewNumberOfPeople = findViewById(R.id.textViewNumberOfPeople);
        TextView textViewFromDate = findViewById(R.id.textViewFromDate);
        TextView textViewToDate = findViewById(R.id.textViewToDate);
        TextView textViewTransportationMode = findViewById(R.id.textViewTransportationMode);
        TextView textViewLocation = findViewById(R.id.textViewLocationSelected);

        // Click listener for the submit button
        submitRatingButton.setOnClickListener(v -> {
            // Rating from the rating bar
            float rating = ratingBar.getRating();

            // Show a notification that the rating has been submitted
            Toast.makeText(TotalPrice.this, "Rating submitted. Thank you!", Toast.LENGTH_SHORT).show();
        });

        // Retrieved and set data for the text views
        textViewTotalPrice.setText("Total Price: $" + getIntent().getIntExtra("totalPrice", 0)); // Get total price from intent
        textViewNumberOfPeople.setText("Number of People: " + mSaveList.loadNumberOfPeople()); // Load the number of people from SaveList
        textViewFromDate.setText("From Date: " + mSaveList.loadFromDate()); // Load the departure date from SaveList
        textViewToDate.setText("To Date: " + mSaveList.loadToDate()); // Load the arrival date from SaveList
        textViewTransportationMode.setText("Transportation Mode: " + mSaveList.loadSelectedTransportation()); // Load the transportation mode from SaveList
        String selectedLocation = mSaveList.loadSelectedLocation(); // Load the selected location from SaveList
        textViewLocation.setText("Location: " + selectedLocation); // Set the selected location
    }
}
