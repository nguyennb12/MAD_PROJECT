package com.example.trip_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private DatabaseHelper db; // Declare DatabaseHelper

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        // Initialize the EditText and Button
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_btn);
        Button regButton = findViewById(R.id.reg_btn);

        // Set a click listener on the login button
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Use DatabaseHelper to check credentials
            if (db.checkUser(username, password)) {
                // Login success
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Intent to start TripPlanner Activity
                Intent intent = new Intent(MainActivity.this, TripPlanner.class);
                startActivity(intent);
            } else {
                // Login failure
                Toast.makeText(MainActivity.this, "Login failed. Please check your username and password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener for the registration button
        regButton.setOnClickListener(v -> {
            // Intent to start Register Activity
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }
}
