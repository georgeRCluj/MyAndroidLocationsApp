package app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils;

import android.location.Location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.MathUtils;

public class LocationUtils {
    private static final String latitudeRegexFormat = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    private static final String longitudeRegexFormat = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";

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

    public static boolean hasLatitudePattern(String latitudeToCheck) {
        Pattern pattern = Pattern.compile(latitudeRegexFormat);
        Matcher emailMatch = pattern.matcher(latitudeToCheck);
        return emailMatch.matches();
    }

    public static boolean hasLongitudePattern(String longitudeToCheck) {
        Pattern pattern = Pattern.compile(longitudeRegexFormat);
        Matcher emailMatch = pattern.matcher(longitudeToCheck);
        return emailMatch.matches();
    }
}
