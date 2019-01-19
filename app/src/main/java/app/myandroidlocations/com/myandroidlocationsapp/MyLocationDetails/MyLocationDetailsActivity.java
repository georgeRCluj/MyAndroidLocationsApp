package app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.EditLocation.EditLocationActivity;
import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.CommunicationUtils;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.GeneralConstants;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityMyLocationDetailsBinding;

public class MyLocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, MyLocationsDetailsNavigator {

    private GoogleMap googleMaps;
    private ActivityMyLocationDetailsBinding binding;
    private int locationClikedId;
    private HashMap<Integer, Marker> markersHash;
    private HashMap<String, MyLocation> myHashLocations;
    private final int DESIRED_ZOOM_LEVEL = 15; // between 2 and 21
    private Marker selectedMarker;
    private LocationDetailsViewModel locationDetailsViewModel;
    private ArrayList<Marker> allMarkers;

    //region Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location_details);
        setToolbar();
        retrieveLocationClicked();
        addMapsFragment();
        initializeViewModel();
        observeMyLocationsList();
        loadFromDbAndAnimateSelectedMarker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeneralConstants.START_EDIT_FROM_DETAILS) {
            if (resultCode == RESULT_OK) {
                removeAllExistingMarkers(); // we need remove the existing markers, in order to show the updated markers; after this
                loadFromDbAndAnimateSelectedMarker();
            }
        }
    }
    //endregion

    //region UI - toolbar
    private void setToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_location_details));
        }
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitleColor));
    }
    //endregion

    //region Retrieve location
    private void retrieveLocationClicked() {
        locationClikedId = getIntent().getIntExtra(GeneralConstants.OPEN_LOCATION_DETAILS_KEY, 0);
    }
    //endregion

    //region Map, markers, click on markers
    private void addMapsFragment() {
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

    private void removeAllExistingMarkers() {
        for (Marker marker : allMarkers) {
            marker.remove();
        }
    }

    private void setActionOnMarkerClick() {
        googleMaps.setOnMarkerClickListener(marker -> {
            selectedMarker = marker;
            binding.setMyLocation(myHashLocations.get(marker.getId()));
            return false;
        });
    }
    //endregion

    //region View model. Observe locations list
    private void initializeViewModel() {
        locationDetailsViewModel = ViewModelProviders.of(this).get(LocationDetailsViewModel.class);
    }

    private void observeMyLocationsList() {
        locationDetailsViewModel.getAllMyLocations().observe(this, myLocations -> {
            if (myLocations != null && myLocations.size() > 0) {
                addMarkersListOnMap(myLocations);
            }
        });
    }
    //endregion

    //region Add markers on map, load from db and animate selected marker
    private void loadFromDbAndAnimateSelectedMarker() {
        LiveData<MyLocation> myLocationLiveData = locationDetailsViewModel.getMyLocationWithId(locationClikedId);
        Observer observer = new Observer<MyLocation>() {
            @Override
            public void onChanged(@Nullable MyLocation myLocation) {
                googleMaps.setOnMapLoadedCallback(() -> {
                    selectedMarker = markersHash.get(myLocation.getId());
                    binding.setMyLocation(myLocation);
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker.getPosition(), DESIRED_ZOOM_LEVEL));
                });
                myLocationLiveData.removeObserver(this);
                // we need to remove this observer, because otherwise, on deletion, the liveData object will fire again and will bring a null object
            }
        };
        myLocationLiveData.observe(this, observer);
    }

    private void addMarkersListOnMap(List<MyLocation> myLocations) {
        markersHash = new HashMap<>();
        allMarkers = new ArrayList<>();
        myHashLocations = new HashMap<>();
        for (MyLocation myLocation : myLocations) {
            LatLng markerLatLang = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(markerLatLang).title(myLocation.getLabel());
            Marker marker = googleMaps.addMarker(markerOptions);
            allMarkers.add(marker);
            markersHash.put(myLocation.getId(), marker);
            myHashLocations.put(marker.getId(), myLocation);
        }
    }
    //endregion

    //region Menu options (update / delete)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_edit_location) {
            this.goToUpdateMyLocation(myHashLocations.get(selectedMarker.getId()).getId());
            return true;
        } else if (id == R.id.action_delete_location) {
            deleteSelectedLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Delete
    private void deleteSelectedLocation() {
        CommunicationUtils.showTwoAnswerDialog(this, getResources().getString(R.string.location_details_delete_confirmation),
                getResources().getString(R.string.location_details_cancel), getResources().getString(R.string.location_details_delete), true, new CommunicationUtils.DoubleButtonClickInterface() {
                    @Override
                    public void onLeftButtonClicked(AlertDialog alertDialog) {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onRightButtonClicked(AlertDialog alertDialog) {
                        locationDetailsViewModel.deleteMyLocationFromDb(myHashLocations.get(selectedMarker.getId()));
                        alertDialog.dismiss();
                        MyLocationDetailsActivity.this.onDeletionSuccessful();
                    }
                });
    }
    //endregion

    //region Navigator
    @Override
    public void onDeletionSuccessful() {
        finish();
    }

    @Override
    public void goToUpdateMyLocation(int myLocationId) {
        startActivityForResult(new Intent(this, EditLocationActivity.class).putExtra(GeneralConstants.EDIT_LOCATION_DETAILS_KEY, myLocationId), GeneralConstants.START_EDIT_FROM_DETAILS);
    }
    //endregion
}
