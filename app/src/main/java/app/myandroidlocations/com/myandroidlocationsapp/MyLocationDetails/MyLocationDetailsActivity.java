package app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.GeneralConstants;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityMyLocationDetailsBinding;

public class MyLocationDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMaps;
    private ActivityMyLocationDetailsBinding binding;
    private int locationClikedId;
    private HashMap<Integer, Marker> markers;
    private HashMap<String, MyLocation> myHashLocations;
    private final int DESIRED_ZOOM_LEVEL = 15; // between 2 and 21
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location_details);
        retrieveLocationClicked();
        addMapsFragment();
        initializeViewModel();
    }

    private void retrieveLocationClicked() {
        locationClikedId = getIntent().getIntExtra(GeneralConstants.OPEN_LOCATION_DETAILS_KEY, 0);
    }

    private void addMapsFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps = googleMap;
        setActionOnMarkerClick();
    }

    private void setActionOnMarkerClick() {
        googleMaps.setOnMarkerClickListener(marker -> {
            selectedMarker = marker;
            binding.setMyLocation(myHashLocations.get(marker.getId()));
            return false;
        });
    }

    //region View model
    private void initializeViewModel() {
        LocationDetailsViewModel locationDetailsViewModel = ViewModelProviders.of(this).get(LocationDetailsViewModel.class);
        locationDetailsViewModel.getAllMyLocations().observe(this, myLocations -> {
            if (myLocations != null && myLocations.size() > 0) {
                addMarkersListOnMap(myLocations);
            }
        });

        locationDetailsViewModel.getMyLocationWithId(locationClikedId).observe(this, myLocation -> googleMaps.setOnMapLoadedCallback(() -> {
            selectedMarker = markers.get(myLocation.getId());
            binding.setMyLocation(myLocation);
            googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker.getPosition(), DESIRED_ZOOM_LEVEL));
        }));
    }

    private void addMarkersListOnMap(List<MyLocation> myLocations) {
        markers = new HashMap<>();
        myHashLocations = new HashMap<>();
        for (MyLocation myLocation : myLocations) {
            LatLng markerLatLang = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(markerLatLang).title(myLocation.getLabel());
            Marker marker = googleMaps.addMarker(markerOptions);
            markers.put(myLocation.getId(), marker);
            myHashLocations.put(marker.getId(), myLocation);
        }

    }
    //endregion
}
