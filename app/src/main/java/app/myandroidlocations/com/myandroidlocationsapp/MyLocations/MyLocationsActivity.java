package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails.MyLocationDetailsActivity;
import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.GeneralConstants;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils.LocationMgr;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.PermissionUtils;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityMyLocationsBinding;
import rx.Observer;

public class MyLocationsActivity extends AppCompatActivity implements MyLocationsNavigator {
    private ActivityMyLocationsBinding binding;
    private MyLocationsNavigator navigator;
    private MyLocationsAdapter myLocationsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_locations);
        navigator = this;
        attachClickListeners();
        initializeRecyclerView();
        requestLocationPermissions();
        initializeLocationManager();
    }

    //region Click listeners
    private void attachClickListeners() {
        binding.fab.setOnClickListener(view -> navigator.goToMyLocationDetails(0));
    }
    //endregion

    //region Initialize recycler view
    private void initializeRecyclerView() {
        myLocationsAdapter = new MyLocationsAdapter(this, this);
        binding.myLocationsRecycler.setHasFixedSize(true);
        binding.myLocationsRecycler.setAdapter(myLocationsAdapter);
        binding.myLocationsRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 10;
            }
        });
    }
    //endregion

    //region Set location manager behavior
    private void initializeLocationManager() {
        LocationMgr locationMgr = new LocationMgr(this);
        locationMgr.getLocationObservable().subscribe(new Observer<Location>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Location location) {
                myLocationsAdapter.refreshMyCurrentLocation(location);
            }
        });
        locationMgr.initialize();
    }
    //endregion

    //region Check location Permission.
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

    private void requestLocationPermissions() {
        if (PermissionUtils.isLocationStoragePermissionGranted(this)) {
            initializeViewModel();
        } else {
            PermissionUtils.checkAndRequestLocationPermissionFromActivity(this);
        }
    }
    //endregion

    //region View model
    private void initializeViewModel() {
        MyLocationsViewModel myLocationsViewModel = ViewModelProviders.of(this).get(MyLocationsViewModel.class);
        myLocationsViewModel.getAllMyLocations().observe(this, myLocations -> {
            if (myLocations != null && myLocations.size() > 0) {
                myLocationsAdapter.refreshMyLocationsList(myLocations);
            }
        });
    }
    //endregion

    //region Navigator
    @Override
    public void goToMyLocationDetails(int clickedLocationId) {
        startActivity(new Intent(this, MyLocationDetailsActivity.class).putExtra(GeneralConstants.OPEN_LOCATION_DETAILS_KEY, clickedLocationId));
    }

    @Override
    public void goToAddLocation() {

    }
    //endregion
}
