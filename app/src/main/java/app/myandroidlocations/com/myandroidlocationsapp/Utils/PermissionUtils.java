package app.myandroidlocations.com.myandroidlocationsapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import app.myandroidlocations.com.myandroidlocationsapp.R;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class PermissionUtils {

    public static boolean isLocationStoragePermissionGranted(Context context) {
        return ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED));
    }

    public static void checkAndRequestLocationPermissionFromActivity(final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog alert = new AlertDialog.Builder(activity)
                        .setTitle(activity.getResources().getString(R.string.location_permission_needed))
                        .setMessage(activity.getResources().getString(R.string.location_permission_message))
                        .setPositiveButton(activity.getResources().getString(R.string.permissions_ok), (dialogInterface, i) ->
                                requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GeneralConstants.PERMISSIONS_REQUEST_LOCATION_FROM_ACTIVITY))
                        .create();
                alert.setOnShowListener(dialog -> alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.permission_positive_btn_color)));
                alert.show();
            } else {
                requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GeneralConstants.PERMISSIONS_REQUEST_LOCATION_FROM_ACTIVITY);
            }
        }
    }
}
