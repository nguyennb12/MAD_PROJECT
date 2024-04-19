package com.example.trip_planner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveList {
    private Context mContext;

    public SaveList(Context context) {
        mContext = context;
    }

    /**
     * Name	    : saveSelectedFromDate
     * Purpose  : To save the departure date which is selected on Datepicker widget.
     * Inputs	: String        selectedFromDate        the date user picked
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public void saveSelectedFromDate(String selectedFromDate) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedFromDate", selectedFromDate);
        editor.apply();
    }

    /**
     * Name	    : saveSelectedToDate
     * Purpose  : To save the arrival date which is selected on Datepicker widget.
     * Inputs	: String        selectedToDate        the date user picked
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public void saveSelectedToDate(String selectedToDate) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedToDate", selectedToDate);
        editor.apply();
    }

    /**
     * Name	    : isValidDate
     * Purpose  : To check the dates user picked is vaild or not
     *            It only can be between departure date and arrival date
     * Inputs	: String        toDate        the date user picked
     *            String        fromDate      the date user picked
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public boolean isValidDate(String toDate, String fromDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date toDateObj = simpleDateFormat.parse(toDate);
            Date fromDateObj = simpleDateFormat.parse(fromDate);

            return !toDateObj.before(fromDateObj);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Name	    : loadFromDate
     * Purpose  : To load departure date data called selectedFromDate saved in second page
     * Inputs	: NONE
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public String loadFromDate() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString("selectedFromDate", "");
    }

    /**
     * Name	    : loadToDate
     * Purpose  : To load arrival date data called selectedToDate saved in second page
     * Inputs	: NONE
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public String loadToDate() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString("selectedToDate", "");
    }

    /**
     * Name	    : saveSelectedTransportation
     * Purpose  : To Save transportation from radio button
     * Inputs	: String       selectedTransportation       A string of transportation
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public void saveSelectedTransportation(String selectedTransportation) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedTransportation", selectedTransportation);
        editor.apply();
    }

    /**
     * Name	    : loadSelectedTransportation
     * Purpose  : To load transportation from radio button
     * Inputs	: NONE
     * Outputs	: NONE
     * Returns	: string of Transportation
     */
    public String loadSelectedTransportation() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString("selectedTransportation", "");
    }

    /**
     * Name	    : saveNumberOfPeople
     * Purpose  : To save the number of people selected by the user
     * Inputs	: int numberOfPeople - the number of people selected
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public void saveNumberOfPeople(int numberOfPeople) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("numberOfPeople", numberOfPeople);
        editor.apply();
    }

    /**
     * Name	    : loadNumberOfPeople
     * Purpose  : To load the number of people previously saved by the user
     * Inputs	: NONE
     * Outputs	: NONE
     * Returns	: int - the number of people previously saved
     */
    public int loadNumberOfPeople() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getInt("numberOfPeople", 1); // Default to 1 if not found
    }

    /**
     * Name	    : saveSelectedLocation
     * Purpose  : To save the selected location
     * Inputs	: String selectedLocation - the selected location
     * Outputs	: NONE
     * Returns	: Nothing
     */
    public void saveSelectedLocation(String selectedLocation) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedLocation", selectedLocation);
        editor.apply();
    }

    /**
     * Name	    : loadSelectedLocation
     * Purpose  : To load the selected location
     * Inputs	: NONE
     * Outputs	: NONE
     * Returns	: String - the selected location
     */
    public String loadSelectedLocation() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString("selectedLocation", "");
    }

}
