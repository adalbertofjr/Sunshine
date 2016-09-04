package br.com.adalbertofjr.sunshine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.sunshine.R;
import br.com.adalbertofjr.sunshine.ui.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String mForecastStr = "";

        if (intent != null) {
            mForecastStr = intent.getDataString();
        }

        if (savedInstanceState == null) {
            DetailFragment df = DetailFragment.newInstance(mForecastStr);
            getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container, df)
                    .commit();
        }
    }
}
