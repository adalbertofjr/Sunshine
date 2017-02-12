package br.com.adalbertofjr.sunshine;

/**
 * Sunshine
 * ForecastAdapter
 * <p/>
 * Created by Adalberto Fernandes Júnior on 28/08/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.adalbertofjr.sunshine.data.WeatherContract;
import br.com.adalbertofjr.sunshine.ui.fragments.ForecastFragment;
import br.com.adalbertofjr.sunshine.util.Utility;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private final Context mContext;
    private boolean mUseTodayLayout = true;
    private Cursor mCursor;
    final private ForecastAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;
    final private ItemChoiceManager mICM;
    boolean useLongToday;

    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler dh, View emptyView, int choiceMode) {
        mContext = context;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = 0;
        if (viewGroup instanceof RecyclerView) {
            // Choose the layout type
            layoutId = -1;
            switch (viewType) {
                case VIEW_TYPE_TODAY: {
                    layoutId = R.layout.list_item_forecast_today;
                    useLongToday = true;
                    break;
                }
                case VIEW_TYPE_FUTURE_DAY: {
                    layoutId = R.layout.list_item_forecast;
                    useLongToday = false;
                    break;
                }
            }

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new ForecastAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        // Read weather icon ID from cursor
        int viewType = getItemViewType(mCursor.getPosition());
        int fallbackIconId;
        int weatherId = mCursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                // Get weather icon
                fallbackIconId = Utility.getArtResourceForWeatherCondition(
                        weatherId);
                break;
            }
            default: {
                // Get weather icon
                fallbackIconId = Utility.getIconResourceForWeatherCondition(
                        weatherId);
                break;
            }
        }

        Glide.with(mContext)
                .load(Utility.getArtUrlForWeatherCondition(mContext, weatherId))
                .error(fallbackIconId)
                .crossFade()
                .into(holder.iconView);

        // this enables better animations. even if we lose state due to a device rotation,
        // the animator can use this to re-find the original view
        ViewCompat.setTransitionName(holder.iconView, "iconView" + position);

        long dateInMillis = mCursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        holder.dateView.setText(Utility.getFriendlyDayString(mContext, dateInMillis, useLongToday));

        String description = mCursor.getString(ForecastFragment.COL_WEATHER_DESC);
        holder.descriptionView.setText(description);
        holder.descriptionView.setContentDescription(mContext.getString(R.string.a11y_forecast, description));

        // Read high temperature from cursor
        String high = Utility.formatTemperature(mContext, mCursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP));
        holder.highTempView.setText(high);
        holder.highTempView.setContentDescription(mContext.getString(R.string.a11y_high_temp, high));

        // Read low temperature from cursor
        String low = Utility.formatTemperature(
                mContext, mCursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
        holder.lowTempView.setText(low);
        holder.lowTempView.setContentDescription(mContext.getString(R.string.a11y_low_temp, low));

        mICM.onBindViewHolder(holder, position);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ForecastAdapterViewHolder) {
            ForecastAdapterViewHolder vfh = (ForecastAdapterViewHolder) viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;
        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        ForecastAdapterViewHolder(View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int dateColumnIndex = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
            mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);
            mICM.onClick(this);
        }
    }

    public static interface ForecastAdapterOnClickHandler {
        void onClick(Long date, ForecastAdapterViewHolder vh);
    }
}