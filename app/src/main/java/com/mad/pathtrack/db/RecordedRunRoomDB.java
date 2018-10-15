package com.mad.pathtrack.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mad.pathtrack.model.RecordedRun;

import java.util.ArrayList;

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
            ArrayList<Location> path = new ArrayList<>();
            Location loc = new Location("");
            loc.setLatitude(-33.904580);
            loc.setLongitude(151.234221);
            path.add(loc);
            Location loc1 = new Location("");
            loc1.setLatitude(-33.900163);
            loc1.setLongitude(151.241041);
            path.add(loc1);
            Location loc2 = new Location("");
            loc2.setLatitude(-33.894371);
            loc2.setLongitude(151.235113);
            path.add(loc2);
            Location loc3 = new Location("");
            loc3.setLatitude(-33.898310);
            loc3.setLongitude(151.228235);
            path.add(loc3);
            run.setPath(path);
            run.pathToString();
            run.calculateDistance();

            RecordedRun run1 = new RecordedRun("Tokyo");
            ArrayList<Location> path1 = new ArrayList<>();
            Location loc4 = new Location("");
            loc4.setLatitude(-33.909324);
            loc4.setLongitude(151.244049);
            path1.add(loc4);
            Location loc5 = new Location("");
            loc5.setLatitude(-33.903055);
            loc5.setLongitude(151.244907);
            path1.add(loc5);
            Location loc6 = new Location("");
            loc6.setLatitude(-33.905114);
            loc6.setLongitude(151.232486);
            path1.add(loc6);
            Location loc7 = new Location("");
            loc7.setLatitude(-33.908640);
            loc7.setLongitude(151.234159);
            path1.add(loc7);
            run1.setPath(path1);
            run1.pathToString();
            run1.calculateDistance();


            RecordedRun run2 = new RecordedRun("Hong Kong");
            ArrayList<Location> path2 = new ArrayList<>();
            Location loc8 = new Location("");
            loc8.setLatitude(-33.910642);
            loc8.setLongitude(151.234116);
            path2.add(loc8);
            Location loc9 = new Location("");
            loc9.setLatitude(-33.914310);
            loc9.setLongitude(151.229441);
            path2.add(loc9);
            Location loc10 = new Location("");
            loc10.setLatitude(-33.910891);
            loc10.setLongitude(151.226224);
            path2.add(loc10);
            Location loc11 = new Location("");
            loc11.setLatitude(-33.907507);
            loc11.setLongitude(151.231714);
            path2.add(loc11);
            run2.setPath(path2);
            run2.pathToString();
            run2.calculateDistance();

            RecordedRun run3 = new RecordedRun("Singapore");
            ArrayList<Location> path3 = new ArrayList<>();
            Location loc12 = new Location("");
            loc12.setLatitude(-33.900576);
            loc12.setLongitude(151.215723);
            path3.add(loc12);
            Location loc13 = new Location("");
            loc13.setLatitude(-33.895660);
            loc13.setLongitude(151.216795);
            path3.add(loc13);
            Location loc14 = new Location("");
            loc14.setLatitude(-33.896138);
            loc14.setLongitude(151.221493);
            path3.add(loc14);
            Location loc15 = new Location("");
            loc15.setLatitude(-33.901267);
            loc15.setLongitude(151.222136);
            path3.add(loc15);
            run3.setPath(path3);
            run3.pathToString();
            run3.calculateDistance();

            mDao.insert(run);
            mDao.insert(run1);
            mDao.insert(run2);
            mDao.insert(run3);

            return null;
        }
    }

}
