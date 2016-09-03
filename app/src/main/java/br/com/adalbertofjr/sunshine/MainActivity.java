package br.com.adalbertofjr.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.sunshine.ui.fragments.ForecastFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FORECASTFRAGMENT_TAG = "forecast_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
    }
}
