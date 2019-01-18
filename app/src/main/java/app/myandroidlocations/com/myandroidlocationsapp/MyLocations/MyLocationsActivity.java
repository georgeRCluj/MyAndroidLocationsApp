package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails.MyLocationDetailsActivity;
import app.myandroidlocations.com.myandroidlocationsapp.R;
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
    }

    private void attachListeners() {
        binding.fab.setOnClickListener(view -> navigator.goToMyLocationDetails());
    }

    @Override
    public void goToMyLocationDetails() {
        startActivity(new Intent(this, MyLocationDetailsActivity.class));
    }
}
