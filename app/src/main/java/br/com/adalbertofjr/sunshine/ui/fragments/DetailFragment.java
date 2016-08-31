package br.com.adalbertofjr.sunshine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.adalbertofjr.sunshine.R;
import br.com.adalbertofjr.sunshine.ui.SettingsActivity;

/**
 * Sunshine
 * DetailFragment
 * <p/>
 * Created by Adalberto Fernandes Júnior on 09/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailFragment extends Fragment {

    private static final String FORECST_SHARE_HASHTAG = " #SunshineApp";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private String mForecastExtra;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(String forecast) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_TEXT, forecast);
        detailFragment.setArguments(args);

        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mForecastExtra = getArguments().getString(Intent.EXTRA_TEXT);

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView forecast = (TextView) rootView.findViewById(R.id.tv_detail_forecast);

        if (!TextUtils.isEmpty(mForecastExtra)) {
            forecast.setText(mForecastExtra);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail_fragment, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        if (myShareActionProvider != null) {
            myShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastExtra + FORECST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {
            Intent settings = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
