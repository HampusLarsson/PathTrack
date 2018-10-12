package com.mad.pathtrack.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mad.pathtrack.model.RecordedRun;

@Database(entities = {RecordedRun.class}, version = 1)
public abstract class RecordedRunRoomDB extends RoomDatabase {

    public abstract RecordedRunDao recordedRunDao();

    private static volatile RecordedRunRoomDB INSTANCE;

    static RecordedRunRoomDB getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized(RecordedRunRoomDB.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecordedRunRoomDB.class,
                            "recorded_run_database").addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final RecordedRunDao mDao;
        PopulateDbAsync(RecordedRunRoomDB db){
            mDao = db.recordedRunDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();
            RecordedRun run = new RecordedRun("Sydney");
            RecordedRun run1 = new RecordedRun("Tokyo");
            RecordedRun run2 = new RecordedRun("Hong Kong");
            RecordedRun run3 = new RecordedRun("Singapore");
            mDao.insert(run);
            mDao.insert(run1);
            mDao.insert(run2);
            mDao.insert(run3);

            return null;
        }
    }

}
