package com.mad.pathtrack.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import java.util.ArrayList;

@Entity(tableName = "recorded_run_table")
public class RecordedRun {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "mDistance")
    private long mDistance;
    @Ignore
    private ArrayList<Location> mPath;
    @ColumnInfo(name = "path")
    private String pathAsString;

    public RecordedRun(String description) {
        mDescription = description;
        mPath = new ArrayList<>();
        mDistance = 0;
        pathAsString = "";

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public long getDistance() {
        return mDistance;
    }

    public void setDistance(long distance) {
        this.mDistance = distance;
    }

    public String getPathAsString() {
        return pathAsString;
    }

    public void setPathAsString(String pathAsString) {
        this.pathAsString = pathAsString;
        stringToPath();
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setPath(ArrayList<Location> path) {
        mPath = path;
    }

    public ArrayList<Location> getPath() {
        return mPath;
    }

    public void pathToString() {
        for (Location l : mPath) {
            pathAsString += Double.toString(l.getLongitude());
            pathAsString += "," + Double.toString(l.getLatitude()) + ",";
        }
    }

    public void stringToPath() {
        if (pathAsString != "" && pathAsString != null) {
            String[] tempPath = pathAsString.split(",");
            for (int i = 0; i < tempPath.length; i = i + 2) {
                Location temp = new Location("");
                if (tempPath[i] != "" && tempPath[i] != null) {
                    temp.setLongitude(Double.parseDouble(tempPath[i]));
                    temp.setLatitude(Double.parseDouble(tempPath[i + 1]));
                    mPath.add(temp);
                }
            }
        }
    }

    public void calculateDistance() {

        for (int i = 0; i < mPath.size(); i++) {
            if (!(i + 2 > mPath.size())) {
                float dist = mPath.get(i).distanceTo(mPath.get(i + 1));
                mDistance += dist;
            }
        }
    }
}
