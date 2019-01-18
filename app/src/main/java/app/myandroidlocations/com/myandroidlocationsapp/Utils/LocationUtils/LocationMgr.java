package app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import rx.subjects.PublishSubject;

public class LocationMgr {
    private LocationManager locationManager;
    private final long LOCATION_INTERVAL_IN_MILLIS = 3000;
    private final float LOCATION_DISTANCE_IN_METERS = 1f;
    private LocationListener locationListener;
    private PublishSubject<Location> locationObservable;

    public LocationMgr(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationObservable = PublishSubject.create();
    }

    @SuppressLint("MissingPermission")
    public void initialize() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationObservable.onNext(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL_IN_MILLIS, LOCATION_DISTANCE_IN_METERS, locationListener);
    }

    public PublishSubject<Location> getLocationObservable() {
        return locationObservable;
    }

}