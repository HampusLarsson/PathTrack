package com.mad.pathtrack.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.mad.pathtrack.db.RecordedRunRepository;
import com.mad.pathtrack.model.RecordedRun;

import java.util.List;

public class RecordedViewModel extends AndroidViewModel {

    private RecordedRunRepository mRepository;
    private LiveData<List<RecordedRun>> mAllRuns;

    public RecordedViewModel(Application application){
        super(application);
        mRepository = new RecordedRunRepository(application);
        mAllRuns = mRepository.getAllRuns();
    }

    public LiveData<List<RecordedRun>> getAllRuns(){
        return mAllRuns;
    }

    public void insert(RecordedRun recordedRun){
        mRepository.insert(recordedRun);

    }


}
