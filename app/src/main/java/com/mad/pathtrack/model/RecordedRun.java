package com.mad.pathtrack.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "recorded_run_table")
public class RecordedRun {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="description")
    private String mDescription;

    public RecordedRun(String description){
        mDescription = description;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String description){
        mDescription = description;
    }
}
