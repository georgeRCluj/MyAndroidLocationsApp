package app.myandroidlocations.com.myandroidlocationsapp.EditLocation;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.CommunicationUtils;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.GeneralConstants;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils.LocationUtils;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityEditLocationBinding;

public class EditLocationActivity extends AppCompatActivity implements EditLocationNavigator {

    private ActivityEditLocationBinding binding;
    private EditLocationViewModel editLocationViewModel;
    private int locationClikedId;
    private MyLocation myLocation;

    //region Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location);
        retrieveLocationClicked();
        initializeViewModel();
        loadLocationDetailsFromDb();
        setActionOnSaveButton();
    }
    //endregion

    //region Retrieve location, initialize view model, load from db
    private void retrieveLocationClicked() {
        locationClikedId = getIntent().getIntExtra(GeneralConstants.EDIT_LOCATION_DETAILS_KEY, 0);
    }

    private void initializeViewModel() {
        editLocationViewModel = ViewModelProviders.of(this).get(EditLocationViewModel.class);
    }

    private void loadLocationDetailsFromDb() {
        editLocationViewModel.getMyLocationWithId(locationClikedId).observe(this, myLocation -> {
            this.myLocation = myLocation;
            binding.setMyLocation(myLocation);
            binding.labelEditText.setSelection(binding.labelEditText.getText().length()); // move cursor at the end of label editText
        });
    }
    //endregion

    //region Click listeners
    private void setActionOnSaveButton() {
        binding.saveButton.setOnClickListener(view -> validateFields());
    }
    //endregion

    //region Validation
    private void validateFields() {
        String label = binding.labelEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();
        String latitude = binding.latitudeEditText.getText().toString().trim();
        String longitude = binding.longitudeEditText.getText().toString().trim();
        final String okText = "OK";

        // firstly check if the label is empty
        if (label.length() == 0) {
            CommunicationUtils.showOneAnswerDialog(EditLocationActivity.this, getResources().getString(R.string.add_location_label_empty), okText, true, Dialog::dismiss);
        }
        // then check if the address is empty
        else if (address.length() == 0) {
            CommunicationUtils.showOneAnswerDialog(EditLocationActivity.this, getResources().getString(R.string.add_location_address_empty), okText, true, Dialog::dismiss);
        }
        // then check the latitude and longitude - to have the proper format
        else if (!LocationUtils.hasLatitudePattern(latitude)) {
            CommunicationUtils.showOneAnswerDialog(EditLocationActivity.this, getResources().getString(R.string.add_location_latitude_invalid), okText, true, Dialog::dismiss);
        } else if (!LocationUtils.hasLongitudePattern(longitude)) {
            CommunicationUtils.showOneAnswerDialog(EditLocationActivity.this, getResources().getString(R.string.add_location_longitude_invalid), okText, true, Dialog::dismiss);
        } else {
            // after all validations, update the model and the DB
            myLocation.setLabel(label);
            myLocation.setAddress(address);
            myLocation.setLatitude(Double.parseDouble(latitude));
            myLocation.setLongitude(Double.parseDouble(longitude));
            editLocationViewModel.updateMyLocation(myLocation);
            this.onEditLocationSuccessful();
        }
    }
    //endregion

    //region Navigator
    @Override
    public void onEditLocationSuccessful() {
        setResult(RESULT_OK);
        finish();
    }
    //endregion
}
