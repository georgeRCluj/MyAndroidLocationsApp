package app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils;

import android.location.Location;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocations.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.MathUtils;

public class LocationUtils {

    public static String getDistanceToCurrentLocation(MyLocation currentLocation, Location myCurrentLocation) {
        String distance = " ";
        Location myLocation = new Location("myCurrentLocation");
        myLocation.setLatitude(currentLocation.getLatitude());
        myLocation.setLongitude(currentLocation.getLongitude());
        if (myCurrentLocation != null) {
            float distanceFloat = myCurrentLocation.distanceTo(myLocation);
            if (distanceFloat < 1000) {
                distance = Math.round(distanceFloat) + " meters away";
            } else {
                distance = MathUtils.round(distanceFloat / 1000, 1) + " km away";
            }
        }
        return distance;
    }
}
