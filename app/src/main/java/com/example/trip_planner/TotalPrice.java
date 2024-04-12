package com.example.trip_planner;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class TotalPrice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);

        // Get the total price passed from TripPlanner activity
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);

        // Find the TextView and set the total price
        TextView textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        textViewTotalPrice.setText(String.format("Total Price: $%d", totalPrice));

        // Setup for RatingBar and submit button
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button submitRatingButton = findViewById(R.id.submitRatingButton);

        submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = "Rating is: " + ratingBar.getRating();
                Toast.makeText(TotalPrice.this, rating, Toast.LENGTH_LONG).show();

                // Construct the string to save
                String fileInfo = "Total Price: $" + totalPrice + "\n" + rating;

                // Call a method to write this string to a file
                writeToFile(fileInfo, TotalPrice.this);
            }
        });
    }

    // Method to write to a file, correctly placed outside onCreate
    private void writeToFile(String data, Context context) {
        String filename = "TripDetails.txt";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes());
            Toast.makeText(context, "Saved to " + context.getFilesDir() + "/" + filename, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("TotalPrice", "File write failed", e);
            Toast.makeText(context, "Failed to save file", Toast.LENGTH_LONG).show();
        }
    }
}
