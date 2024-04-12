package com.example.trip_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private DatabaseHelper db; // DatabaseHelper object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this); // Initialize the DatabaseHelper

        EditText usernameEditText = findViewById(R.id.reg_username);
        EditText passwordEditText = findViewById(R.id.reg_password);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password); // Assuming you add this field
        EditText emailEditText = findViewById(R.id.reg_email);
        Button regBtn = findViewById(R.id.reg_btn);
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);


        regBtn.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim(); // Confirm password
            String email = emailEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simple email pattern check
            if (!Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(email).matches()) {
                Toast.makeText(Register.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to add user to the database, including the email
            boolean insertSuccess = db.addUser(username, password, email);
            if (insertSuccess) {
                Toast.makeText(Register.this, username + " has officially created an account", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, TripPlanner.class);
                startActivity(intent);
            } else {
                Toast.makeText(Register.this, "Registration failed. Email may already be in use.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
