package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.CONTENT_URI;

//  COMP (1) Create a class called SunshineSyncTask
//  COMP (2) Within SunshineSyncTask, create a synchronized public static void method called
// syncWeather
public class SunshineSyncTask {

    synchronized public static void syncWeather(@NonNull final Context context) {
//      COMP (3) Within syncWeather, fetch new weather data
        try {
            URL weatherURL = NetworkUtils.getUrl(context);
            String request = NetworkUtils.getResponseFromHttpUrl(weatherURL);

            ContentValues[] getResults = OpenWeatherJsonUtils.getWeatherContentValuesFromJson
                    (context, request);

            if (getResults != null && getResults.length != 0) {
//      COMP (4) If we have valid results, delete the old data and insert the new
                ContentResolver sunshineContentResolver = context.getContentResolver();
                sunshineContentResolver.delete(
                        CONTENT_URI,
                        null,
                        null);

                sunshineContentResolver.bulkInsert(
                        CONTENT_URI,
                        getResults);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
