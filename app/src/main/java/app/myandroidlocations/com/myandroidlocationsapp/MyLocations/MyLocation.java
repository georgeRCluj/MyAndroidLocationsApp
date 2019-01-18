package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "my_locations")

public class MyLocation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public int id;

    @SerializedName("label")
    @ColumnInfo
    @NonNull
    public String label;

    @SerializedName("address")
    @ColumnInfo
    @NonNull
    public String address;

    @SerializedName("latitude")
    @ColumnInfo
    @NonNull
    public double latitude;

    @SerializedName("longitude")
    @ColumnInfo
    @NonNull
    public double longitude;

    public MyLocation(String label, String address, double latitude, double longitude) {
        this.label = label;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}