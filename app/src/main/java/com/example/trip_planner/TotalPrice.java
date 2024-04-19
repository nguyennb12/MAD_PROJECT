package com.example.trip_planner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class TotalPrice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);

        // Assuming you have your rating bar and submit button setup in your XML layout
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button submitRatingButton = findViewById(R.id.submitRatingButton);

        // Assuming you have your text views setup in your XML layout
        TextView textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        TextView textViewNumberOfPeople = findViewById(R.id.textViewNumberOfPeople);
        TextView textViewFromDate = findViewById(R.id.textViewFromDate);
        TextView textViewToDate = findViewById(R.id.textViewToDate);
        TextView textViewTransportationMode = findViewById(R.id.textViewTransportationMode);
        TextView textViewLocation = findViewById(R.id.textViewLocationSelected);

        // Set up a click listener for the submit button
        submitRatingButton.setOnClickListener(v -> {
            // Assuming you have obtained the rating from the rating bar
            float rating = ratingBar.getRating();

            // Your logic to submit the rating goes here

            // Show a notification that the rating has been submitted
            Toast.makeText(TotalPrice.this, "Rating submitted. Thank you!", Toast.LENGTH_SHORT).show();
        });

        // Assuming you have retrieved and set the data for the text views
        textViewTotalPrice.setText("Total Price: $100");
        textViewNumberOfPeople.setText("Number of People: 4");
        textViewFromDate.setText("From Date: 2024-04-20");
        textViewToDate.setText("To Date: 2024-04-25");
        textViewTransportationMode.setText("Transportation Mode: Flight");
        textViewLocation.setText("Location: Paris");
    }
}
