package com.mad.pathtrack.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mad.pathtrack.R;
import com.mad.pathtrack.model.RecordedRun;
import com.mad.pathtrack.viewModel.RecordedViewModel;

import java.util.List;

public class RecordedActivity extends AppCompatActivity {
    private RecordedViewModel mRecordedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded);

        mRecordedViewModel = ViewModelProviders.of(this).get(RecordedViewModel.class);


        RecyclerView recyclerView = findViewById(R.id.recorded_recycler_view);
        final RecordedRunAdapter adapter = new RecordedRunAdapter(this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recyclerView.addItemDecoration(itemDecor);

        mRecordedViewModel.getAllRuns().observe(this, new Observer<List<RecordedRun>>() {
            @Override
            public void onChanged(@Nullable final List<RecordedRun> runs) {
                // Update the cached copy of the words in the adapter.
                adapter.setRuns(runs);
            }
        });

    }
}
