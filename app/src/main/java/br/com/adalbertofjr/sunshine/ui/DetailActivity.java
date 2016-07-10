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
        String forecast = intent.getStringExtra(Intent.EXTRA_TEXT);

        DetailFragment df = DetailFragment.newInstance(forecast);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_detail_container, df, null)
                .commit();

    }
}
