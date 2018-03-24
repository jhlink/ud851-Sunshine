package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static android.app.Notification.EXTRA_TEXT;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView todaysWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        todaysWeatherTextView = (TextView) findViewById(R.id.tv_detail_view_todays_weather);
        Intent receivedIntent = getIntent();

        if (receivedIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String weatherData = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
            todaysWeatherTextView.setText(weatherData);
        }
    }
}