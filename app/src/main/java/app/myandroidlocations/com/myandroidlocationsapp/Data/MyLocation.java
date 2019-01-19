package app.myandroidlocations.com.myandroidlocationsapp.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "my_locations",
        indices = {@Index("label")})

public class MyLocation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int id;

    @SerializedName("label")
    @ColumnInfo
    @NonNull
    private String label;

    @SerializedName("address")
    @ColumnInfo
    @NonNull
    private String address;

    @SerializedName("latitude")
    @ColumnInfo
    @NonNull
    private double latitude;

    @SerializedName("longitude")
    @ColumnInfo
    @NonNull
    private double longitude;

    public String getLabel() {
        return label;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStringLatitude() {
        return String.valueOf(latitude);
    }

    public String getStringLongitude() {
        return String.valueOf(longitude);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public MyLocation(String label, String address, double latitude, double longitude) {
        this.label = label;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}