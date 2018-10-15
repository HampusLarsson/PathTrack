package com.mad.pathtrack.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.mad.pathtrack.model.RecordedRun;

import java.util.List;

public class RecordedRunRepository {
    private RecordedRunDao mRecordedRunDao;
    private LiveData<List<RecordedRun>> mAllRuns;

    public RecordedRunRepository(Application application) {
        RecordedRunRoomDB db = RecordedRunRoomDB.getDatabase(application);
        mRecordedRunDao = db.recordedRunDao();
        mAllRuns = mRecordedRunDao.getAllRuns();
    }

    public LiveData<List<RecordedRun>> getAllRuns() {
        return mAllRuns;
    }

    public void insert(RecordedRun recordedRun) {
        new insertAsyncTask(mRecordedRunDao).execute(recordedRun);
    }

    public RecordedRun getRunById(int id) {
        return mRecordedRunDao.findById(id);
    }

    private static class insertAsyncTask extends AsyncTask<RecordedRun, Void, Void> {
        private RecordedRunDao mAsyncDao;

        public insertAsyncTask(RecordedRunDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final RecordedRun... params) {
            mAsyncDao.insert(params[0]);
            return null;
        }

    }
}
