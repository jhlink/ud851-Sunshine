/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import org.w3c.dom.Text;

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.*;

public class DetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
//      COMP (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  COMP (18) Create a String array containing the names of the desired data columns from our ContentProvider
    public String[] DETAIL_WEATHER_PROJECTION = new String[] {
        COLUMN_DATE,
        COLUMN_WEATHER_ID,
        COLUMN_MAX_TEMP,
        COLUMN_MIN_TEMP,
        COLUMN_HUMIDITY,
        COLUMN_WIND_SPEED,
        COLUMN_DEGREES,
        COLUMN_PRESSURE
    };

//  COMP (19) Create constant int values representing each column name's position above
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_ID = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 2;
    public static final int INDEX_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_HUMIDITY = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_WIND_DEGREE = 6;
    public static final int INDEX_WEATHER_PRESSURE = 7;

//  COMP (20) Create a constant int to identify our loader used in DetailActivity
    public static final int DETAIL_ACTIVITY_LOADER_ID = 42;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  COMP (15) Declare a private Uri field called mUri
    private Uri mUri;

//  COMP (10) Remove the mWeatherDisplay TextView declaration

//  COMP (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mWeatherDate;
    private TextView mWeatherDescription;
    private TextView mWeatherHighTemp;
    private TextView mWeatherLowTemp;
    private TextView mWeatherHumidity;
    private TextView mWeatherWind;
    private TextView mWeatherPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      COMP (12) Remove mWeatherDisplay TextView
//      COMP (13) Find each of the TextViews by ID
        mWeatherDate = (TextView) findViewById(R.id.tv_detail_date);
        mWeatherDescription = (TextView) findViewById(R.id.tv_detail_description);
        mWeatherHighTemp = (TextView) findViewById(R.id.tv_detail_high_temp);
        mWeatherLowTemp = (TextView) findViewById(R.id.tv_detail_low_temp);
        mWeatherHumidity = (TextView) findViewById(R.id.tv_detail_humidity);
        mWeatherWind = (TextView) findViewById(R.id.tv_detail_wind);
        mWeatherPressure = (TextView) findViewById(R.id.tv_detail_pressure);

//      COMP (14) Remove the code that checks for extra text
        mUri = getIntent().getData();
        if (mUri == null) {
            throw new NullPointerException("No reference to Uri in Intent");
        }
//      COMP (16) Use getData to get a reference to the URI passed with this Activity's Intent
//      COMP (17) Throw a NullPointerException if that URI is null

//      COMP (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(
                DETAIL_ACTIVITY_LOADER_ID,
                null,
                this);


    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  COMP (22) Override onCreateLoader
//      COMP (23) If the loader requested is our detail loader, return the appropriate CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id)  {
            case DETAIL_ACTIVITY_LOADER_ID:

               return new CursorLoader(
                       this,
                       mUri,
                       DETAIL_WEATHER_PROJECTION,
                       null,
                       null,
                       null);

            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    private void displayDataInTextViews(Cursor data) {
        if (data != null) {
            long normalizedDate = data.getLong(INDEX_WEATHER_DATE);
            String readableDate = SunshineDateUtils.getFriendlyDateString(
                    this,
                    normalizedDate,
                    false
            );
            mWeatherDate.setText(readableDate);

            int sWeatherID = data.getInt(INDEX_WEATHER_ID);
            String weatherDescription = SunshineWeatherUtils.getStringForWeatherCondition(
                    this,
                    sWeatherID
            );
            mWeatherDescription.setText(weatherDescription);

            double sHighTempValue = data.getDouble(INDEX_WEATHER_MAX_TEMP);
            String sHighTempInCels = SunshineWeatherUtils.formatTemperature(this, sHighTempValue);
            mWeatherHighTemp.setText(sHighTempInCels);

            double sLowTempValue = data.getDouble(INDEX_WEATHER_MIN_TEMP);
            String sLowTempInCels = SunshineWeatherUtils.formatTemperature(this, sLowTempValue);
            mWeatherLowTemp.setText(sLowTempInCels);

            String formattedTemps = SunshineWeatherUtils.formatHighLows(
                    this,
                    sHighTempValue,
                    sLowTempValue
            );

            String sHumidity = getString(R.string.format_humidity, data.getFloat
                    (INDEX_WEATHER_HUMIDITY));
            mWeatherHumidity.setText(sHumidity);

            float sWindSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
            float sWindDir = data.getFloat(INDEX_WEATHER_WIND_DEGREE);
            String formattedWindSpeedAndDir = SunshineWeatherUtils.getFormattedWind(
                    this,
                    sWindSpeed,
                    sWindDir
            );
            mWeatherWind.setText(formattedWindSpeedAndDir);

            String sPressure = getString(R.string.format_pressure, data.getDouble(INDEX_WEATHER_PRESSURE));
            mWeatherPressure.setText(sPressure);

            mForecastSummary = String.format("%s - %s - %s/%s",
                    readableDate,
                    weatherDescription,
                    sHighTempInCels,
                    sLowTempInCels);
        }
    }


    //  COMP (24) Override onLoadFinished
//      COMP (25) Check before doing anything that the Cursor has valid data
//      COMP (26) Display a readable data string
//      COMP (27) Display the weather description (using SunshineWeatherUtils)
//      COMP (28) Display the high temperature
//      COMP (29) Display the low temperature
//      COMP (30) Display the humidity
//      COMP (31) Display the wind speed and direction
//      COMP (32) Display the pressure
//      COMP (33) Store a forecast summary in mForecastSummary
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            data.moveToFirst();
            displayDataInTextViews(data);
        }
    }


//  COMP (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}