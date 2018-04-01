/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_DATE;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_MAX_TEMP;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_MIN_TEMP;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_WEATHER_ID;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    //  COMP (1) Declare a private final Context field called mContext
    private final Context mContext;
    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private ForecastAdapterOnClickHandler mClickHandler;
    //  TODO (14) Remove the mWeatherData declaration and the setWeatherData method
    private String[] mWeatherData;
    //  COMP (2) Declare a private Cursor field called mCursor
    private Cursor mCursor = null;

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

//  COMP (3) Add a Context field to the constructor and store that context in mContext

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
//      COMP (5) Delete the current body of onBindViewHolder
//      COMP (6) Move the cursor to the appropriate position
        if (mCursor != null) {
            mCursor.moveToPosition(position);

//      COMP (7) Generate a weather summary with the date, description, high and low

            String weatherForThisDay = readAndComposeWeatherData();

//      COMP (8) Display the summary that you created above

            forecastAdapterViewHolder.weatherSummary.setText(weatherForThisDay);
        }
    }

    private String readAndComposeWeatherData() {
        String result = "";
        if (mCursor != null) {
            int weatherDateColIndex = mCursor.getColumnIndex(COLUMN_DATE);
            long normalizedWeatherDate = mCursor.getLong(weatherDateColIndex);
            String friendlyWeatherDate = SunshineDateUtils.getFriendlyDateString(mContext,
                    normalizedWeatherDate, true);

            int weatherDescColIndex = mCursor.getColumnIndex(COLUMN_WEATHER_ID);
            int weatherId = mCursor.getInt(weatherDescColIndex);
            String weatherDesc = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);

            int weatherHighColIndex = mCursor.getColumnIndex(COLUMN_MAX_TEMP);
            double weatherHighTemp = mCursor.getDouble(weatherHighColIndex);

            int weatherLowColIndex = mCursor.getColumnIndex(COLUMN_MIN_TEMP);
            double weatherLowTemp = mCursor.getDouble(weatherLowColIndex);
            String formatHighsLows = SunshineWeatherUtils.formatHighLows(mContext,
                    weatherHighTemp,weatherLowTemp);

            result = friendlyWeatherDate + ": " + weatherDesc + " -- " + formatHighsLows;
        }

        return result;
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
//      COMP (9) Delete the current body of getItemCount
//      COMP (10) If mCursor is null, return 0. Otherwise, return the count of mCursor
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param weatherData The new weather data to be displayed.
     */
    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    //  COMP (11) Create a new method that allows you to swap Cursors.
    //      COMP (12) After the new Cursor is set, call notifyDataSetChanged
    public void swapCursor(Cursor c) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = c;

        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView weatherSummary;

        ForecastAdapterViewHolder(View view) {
            super(view);
            weatherSummary = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            //  COMP (13) Instead of passing the String from the data array, use the weatherSummary
            // text!

            final TextView weatherSummary = (TextView) v.findViewById(R.id.tv_weather_data);
            String weatherForDay = weatherSummary.getText().toString();
            mClickHandler.onClick(weatherForDay);
        }
    }
}