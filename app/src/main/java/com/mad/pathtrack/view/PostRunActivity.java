package com.mad.pathtrack.view;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.mad.pathtrack.R;

import static com.mad.pathtrack.view.MapsActivity.DESCRIPTION_KEY;
import static com.mad.pathtrack.view.MapsActivity.DISTANCE_KEY;

public class PostRunActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_run);
        Intent intent = getIntent();
        String description = intent.getStringExtra(DESCRIPTION_KEY);
        long distance = intent.getLongExtra(DISTANCE_KEY, 0);

        Resources res = getResources();
        String temp = "";

        TextView titleTv = findViewById(R.id.post_title_tv);
        titleTv.setText(R.string.post_title);

        TextView descriptionTv = findViewById(R.id.post_description_tv);
        temp = res.getString(R.string.post_description, description);
        descriptionTv.setText(temp);

        TextView distanceTv = findViewById(R.id.post_distance_tv);
        temp = res.getString(R.string.post_distance, Long.toString(distance));
        distanceTv.setText(temp);

        ButtonRectangle finishtBtn = findViewById(R.id.post_finish_btn);
        finishtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
