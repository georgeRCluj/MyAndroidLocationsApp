package app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityMyLocationDetailsBinding;

public class MyLocationDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMaps;
    private ActivityMyLocationDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location_details);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMaps.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
