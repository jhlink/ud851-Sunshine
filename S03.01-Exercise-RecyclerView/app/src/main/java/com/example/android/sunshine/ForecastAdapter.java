package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 * Created by james on 3/22/18.
 */

public class ForecastAdapter extends  RecyclerView.Adapter<ForecastAdapter
        .ForecastAdapterViewHolder> {

    private static final String TAG = ForecastAdapter.class.getSimpleName();
    private String[] mWeatherData = null;

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int forecastListItemXML = R.layout.forecast_list_item;
        boolean shouldAttachedImmediatelyToParent = true;

        View view = inflater.inflate(forecastListItemXML, parent, shouldAttachedImmediatelyToParent);
        ForecastAdapterViewHolder viewHolder = new ForecastAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        Log.v(TAG, "Forecast #" + position);
        holder.bind(mWeatherData[position]);
    }

    @Override
    public int getItemCount() {
        if (mWeatherData != null) {
            return mWeatherData.length;
        }
        return 0;
    }

    void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData.clone();
        notifyDataSetChanged();
    }


    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View inputView) {
            super(inputView);
            mWeatherTextView = (TextView) inputView.findViewById(R.layout
                    .forecast_list_item);
        }

        public void bind(String viewWeather) {
            mWeatherTextView.setText(viewWeather);
        }
    }
}
