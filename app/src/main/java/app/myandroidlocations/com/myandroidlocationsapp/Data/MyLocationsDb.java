package app.myandroidlocations.com.myandroidlocationsapp.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocations.MyLocation;

@Database(entities = {MyLocation.class},
        version = 1,
        exportSchema = false)
public abstract class MyLocationsDb extends RoomDatabase {

    private static MyLocationsDb INSTANCE;
    private static final String DB_NAME = "MyLocations.db";

    public static MyLocationsDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyLocationsDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyLocationsDb.class, DB_NAME)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract MyLocationDao myLocationDao();
}
