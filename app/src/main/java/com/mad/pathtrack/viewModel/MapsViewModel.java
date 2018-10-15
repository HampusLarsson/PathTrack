package com.mad.pathtrack.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.location.Location;

import com.mad.pathtrack.db.RecordedRunRepository;
import com.mad.pathtrack.model.RecordedRun;

import java.util.ArrayList;

public class MapsViewModel extends AndroidViewModel {
    private RecordedRun mCurrentRun;
    private ArrayList<Location> mCurrentPath;
    private RecordedRunRepository mRepository;

    public MapsViewModel(Application application){
        super(application);

        mRepository = new RecordedRunRepository(application);
        mCurrentPath = new ArrayList<>();

    }

    public RecordedRun getCurrentRun(){
        return mCurrentRun;
    }

    public void getRunById(int id){
       mCurrentRun =  mRepository.getRunById(id);
       mCurrentRun.stringToPath();
    }

    public void setCurrentRun(RecordedRun run){
        mCurrentRun = run;
    }

    public void setCurrentPath(ArrayList<Location> locations){
        mCurrentPath = locations;
    }

    public void addLocation(Location location){
        mCurrentPath.add(location);

    }

    public void startRecording(RecordedRun recordedRun){
        mCurrentRun = recordedRun;
    }

    public void insert(){
        mCurrentRun.setPath(mCurrentPath);
        mCurrentRun.pathToString();
        mCurrentRun.calculateDistance();
        mRepository.insert(mCurrentRun);
    }
}
