package com.example.trip_planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//asdf
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TripPlanner.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table creation for registered users
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT, " +
                        "password TEXT, " +
                        "email TEXT UNIQUE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database version upgrades here
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Method to add user to the database
    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        long result = db.insert("users", null, values);
        return result != -1; // Return true if the insert is successful
    }

    // Method to check user login credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"id"}, // Columns to return
                "username=? AND password=?", // Selection criteria
                new String[]{username, password}, // Selection criteria
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0; // True if user exists, false otherwise
    }
}
