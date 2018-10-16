package com.mad.pathtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.gc.materialdesign.views.ButtonRectangle;
import com.mad.pathtrack.R;

import static com.mad.pathtrack.view.MapsActivity.RECORD_KEY;
import static com.mad.pathtrack.view.RecordedRunAdapter.RecordedRunViewHolder.TYPE_KEY;

public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonRectangle showMapButton = findViewById(R.id.show_map_button);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra(TYPE_KEY, RECORD_KEY);
                startActivity(intent);
            }
        });

        ButtonRectangle showRecordedButton = findViewById(R.id.show_recorded_button);
        showRecordedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecordedActivity.class);
                startActivity(intent);
            }
        });

        ButtonRectangle showRecommendedButton = findViewById(R.id.show_recommended_button);
        showRecommendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecommendedActivity.class);
                startActivity(intent);
            }
        });




    }





}
