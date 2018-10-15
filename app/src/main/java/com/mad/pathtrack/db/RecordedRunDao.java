package com.mad.pathtrack.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mad.pathtrack.model.RecordedRun;

import java.util.List;

@Dao
public interface RecordedRunDao {

    @Insert
    void insert(RecordedRun recordedRun);
    @Query("DELETE FROM recorded_run_table")
    void deleteAll();
    @Query("SELECT * FROM recorded_run_table")
    LiveData<List<RecordedRun>> getAllRuns();
    @Query("SELECT * FROM recorded_run_table WHERE mId= :id")
    RecordedRun findById(int id);


}
