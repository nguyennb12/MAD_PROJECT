package com.example.trip_planner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.ActivityNotFoundException;
import android.net.Uri;

public class TripPlanner extends AppCompatActivity {

    private Spinner spinnerNumberOfPeople;
    private int numberOfPeople = 1; // Default number of people

    // URLs for travel guides
    private final String urlParisGuide = "https://irp.cdn-website.com/3959ca2f/files/uploaded/PARIS-%20FRANCE%20Travel%20Guide.pdf";
    private final String urlLondonGuide = "https://content.tfl.gov.uk/london-visitor-guide.pdf";
    private final String urlTorontoGuide = "https://guides.tripomatic.com/download/tripomatic-free-city-guide-toronto.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);

        // Initialize components
        initializeComponents();
    }

    private void initializeComponents() {
        spinnerNumberOfPeople = findViewById(R.id.spinnerNumberOfPeople);
        setupSpinner();
        setupPriceButtons();
        setupDownloadButtons();
        setupVisitButtons();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_travelers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumberOfPeople.setAdapter(adapter);
        spinnerNumberOfPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numberOfPeople = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numberOfPeople = 1; // Default to 1 if nothing is selected
            }
        });
    }

    private void setupPriceButtons() {
        Button btnParis = findViewById(R.id.btnParis);
        Button btnLondon = findViewById(R.id.btnLondon);
        Button btnToronto = findViewById(R.id.btnToronto);

        btnParis.setOnClickListener(v -> navigateToTotalPriceActivity(250));
        btnLondon.setOnClickListener(v -> navigateToTotalPriceActivity(500));
        btnToronto.setOnClickListener(v -> navigateToTotalPriceActivity(1000));
    }

    private void setupDownloadButtons() {
        Button btnDownloadParis = findViewById(R.id.btnDownloadParis);
        Button btnDownloadLondon = findViewById(R.id.btnDownloadLondon);
        Button btnDownloadToronto = findViewById(R.id.btnDownloadToronto);

        btnDownloadParis.setOnClickListener(v -> downloadGuide(urlParisGuide, "paris_guide.pdf", this::openDownloadedFile));
        btnDownloadLondon.setOnClickListener(v -> downloadGuide(urlLondonGuide, "london_guide.pdf", this::openDownloadedFile));
        btnDownloadToronto.setOnClickListener(v -> downloadGuide(urlTorontoGuide, "toronto_guide.pdf", this::openDownloadedFile));
    }

    private void setupVisitButtons() {
        Button btnVisitToronto = findViewById(R.id.btnVisitToronto);
        Button btnVisitLondon = findViewById(R.id.btnVisitLondon);
        Button btnVisitParis = findViewById(R.id.btnVisitParis);

        btnVisitToronto.setOnClickListener(v -> visitWebsite("https://www.toronto.ca/"));
        btnVisitLondon.setOnClickListener(v -> visitWebsite("https://london.ca/"));
        btnVisitParis.setOnClickListener(v -> visitWebsite("https://www.france.fr/en/paris"));
    }

    private void visitWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found to open this URL", Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToTotalPriceActivity(int pricePerPerson) {
        int totalPrice = numberOfPeople * pricePerPerson;
        Intent intent = new Intent(this, TotalPrice.class);
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }

    private void openDownloadedFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found to open this file", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadGuide(String fileUrl, String fileName, DownloadCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName))) {
                    byte[] data = new byte[1024];
                    int count;
                    while ((count = inputStream.read(data)) != -1) {
                        fileOutputStream.write(data, 0, count);
                    }
                }
                File outputFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                Log.i("TripPlanner", "Guide downloaded successfully: " + fileName);
                runOnUiThread(() -> callback.onDownloadComplete(outputFile));
            } catch (Exception e) {
                Log.e("TripPlanner", "Error downloading guide", e);
                runOnUiThread(() -> Toast.makeText(TripPlanner.this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    // Callback interface for download completion
    interface DownloadCallback {
        void onDownloadComplete(File file);
    }
}
