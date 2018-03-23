package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 *
 * Created by james on 3/22/18.
 */

public class ForecastAdapter extends  RecyclerView.Adapter<ForecastAdapter
        .ForecastAdapterViewHolder> {

    private String[] mWeatherData;

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View inputView) {
            super(inputView);
            mWeatherTextView = (TextView) inputView.findViewById(R.layout
                    .forecast_list_item);
        }
    }
}
