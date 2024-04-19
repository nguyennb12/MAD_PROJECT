package com.example.trip_planner;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class TripPlanner extends AppCompatActivity {

    private Spinner spinnerNumberOfPeople;
    private int numberOfPeople = 1; // Default number of people

    // URLs for travel guides
    private final String urlParisGuide = "https://irp.cdn-website.com/3959ca2f/files/uploaded/PARIS-%20FRANCE%20Travel%20Guide.pdf";
    private final String urlLondonGuide = "https://content.tfl.gov.uk/london-visitor-guide.pdf";
    private final String urlTorontoGuide = "https://guides.tripomatic.com/download/tripomatic-free-city-guide-toronto.pdf";

    Button fromDateButton = null;
    Button toDateButton = null;
    private SaveList mSaveList; // save and load date information. - in this case, using SharedPreferences.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);

        // Initialize components
        initializeComponents();

        mSaveList = new SaveList(this);

        ////////////////////////////////////////////////////////
        // Action for a button that specifies a departure date
        fromDateButton = findViewById(R.id.fromButtonId);
        TextView fromDateTextView = findViewById(R.id.fromDateId);
        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This declaration is needed to make DatePicker widget to pick departure date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TripPlanner.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String selectedFromDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                                mSaveList.saveSelectedFromDate(selectedFromDate);

                                fromDateTextView.setText(selectedFromDate);
                            }
                        },
                        year,
                        month,
                        day
                );
                // Show up DatePickerDialog
                datePickerDialog.show();
            }
        });

        // Action for a button that specifies a arrival date
        toDateButton = findViewById(R.id.toButtonId);
        TextView toDateTextView = findViewById(R.id.toDateId);
        toDateButton.setOnClickListener(new View.OnClickListener() {
            // This declaration is needed to make DatePicker widget to pick arrival date
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TripPlanner.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String selectedToDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                                String fromDateText = fromDateTextView.getText().toString();

                                // To check the arrival date is formal of departure date or not
                                if (mSaveList.isValidDate(selectedToDate, fromDateText)) {
                                    mSaveList.saveSelectedToDate(selectedToDate);
                                    toDateTextView.setText(selectedToDate);
                                } else {
                                    Toast t = Toast.makeText(TripPlanner.this,
                                            "Invalid date selection. toDate should not be before fromDate.", Toast.LENGTH_SHORT);
                                    t.show();
                                }
                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
            }
        });

        // Select the transportation
        Spinner transportationSpinner = findViewById(R.id.spinnerTransportation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                TripPlanner.this, R.array.spinnerTransportationArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        transportationSpinner.setAdapter(adapter);

        // Save the transportation whenever it changes
        transportationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTransportation = parent.getItemAtPosition(position).toString();
                mSaveList.saveSelectedTransportation(selectedTransportation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        /////////////////////////////////////////////

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
                mSaveList.saveNumberOfPeople(numberOfPeople); // Save the updated number of people
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

        btnParis.setOnClickListener(v -> navigateToTotalPriceActivity(250, "Paris"));
        btnLondon.setOnClickListener(v -> navigateToTotalPriceActivity(500, "London"));
        btnToronto.setOnClickListener(v -> navigateToTotalPriceActivity(1000, "Toronto"));
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

    private void navigateToTotalPriceActivity(int pricePerPerson, String selectedLocation) {
        int totalPrice = numberOfPeople * pricePerPerson;
        mSaveList.saveSelectedLocation(selectedLocation); // Save the selected location
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
