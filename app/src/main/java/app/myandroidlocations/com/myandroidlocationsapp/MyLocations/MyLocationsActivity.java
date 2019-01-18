package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails.MyLocationDetailsActivity;
import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.GeneralConstants;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.PermissionUtils;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityMyLocationsBinding;

public class MyLocationsActivity extends AppCompatActivity implements MyLocationsNavigator {
    private ActivityMyLocationsBinding binding;
    private MyLocationsNavigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_locations);
        navigator = this;
        attachListeners();
        requestLocationPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case GeneralConstants.PERMISSIONS_REQUEST_LOCATION_FROM_ACTIVITY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeViewModel();
                } else {
                    PermissionUtils.checkAndRequestLocationPermissionFromActivity(this);
                }
                break;
            }
        }
    }

    private void attachListeners() {
        binding.fab.setOnClickListener(view -> navigator.goToMyLocationDetails());
    }

    private void requestLocationPermissions() {
        if (PermissionUtils.isLocationStoragePermissionGranted(this)) {
            initializeViewModel();
        } else {
            PermissionUtils.checkAndRequestLocationPermissionFromActivity(this);
        }
    }

    private void initializeViewModel() {
        MyLocationsViewModel myLocationsViewModel = ViewModelProviders.of(this).get(MyLocationsViewModel.class);
        myLocationsViewModel.getAllMyLocations().observe(this, myLocations -> Log.d("ROOM_TEST", "size = " + (myLocations != null ? myLocations.size() : "null")));
    }

    //region Navigator
    @Override
    public void goToMyLocationDetails() {
        startActivity(new Intent(this, MyLocationDetailsActivity.class));
    }
    //endregion
}
