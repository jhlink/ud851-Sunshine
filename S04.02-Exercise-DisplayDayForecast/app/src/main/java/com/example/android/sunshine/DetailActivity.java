package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView todaysWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        todaysWeatherTextView = (TextView) findViewById(R.id.tv_detail_view_todays_weather);
        String weatherData = getIntent().getStringExtra("TODAYS_WEATHER");
        todaysWeatherTextView.setText(weatherData);
    }
}