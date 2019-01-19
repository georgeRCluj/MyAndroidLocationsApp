package app.myandroidlocations.com.myandroidlocationsapp.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocations.MyLocation;

@Dao
interface MyLocationDao {

    @Query("SELECT * FROM my_locations")
    LiveData<List<MyLocation>> getAllMyLocations();

    @Query("SELECT * FROM my_locations WHERE id = :id")
    LiveData<MyLocation> getMyLocationWithId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MyLocation myLocation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<MyLocation> myLocations);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(MyLocation myLocation);

    @Delete
    void deleteMyLocation(MyLocation myLocation);

    @Query("DELETE FROM my_locations")
    void deleteAll();

}
