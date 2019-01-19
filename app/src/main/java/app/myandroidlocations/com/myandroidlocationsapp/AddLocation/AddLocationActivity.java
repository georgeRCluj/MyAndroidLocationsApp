package app.myandroidlocations.com.myandroidlocationsapp.AddLocation;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.CommunicationUtils;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils.LocationUtils;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityAddLocationBinding;

public class AddLocationActivity extends AppCompatActivity implements AddLocationNavigator{

    private ActivityAddLocationBinding binding;
    private AddLocationViewModel addLocationViewModel;

    //region Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_location);
        initializeViewModel();
        setActionOnSaveButton();
    }
    //endregion

    //region View model
    private void initializeViewModel() {
        addLocationViewModel = ViewModelProviders.of(this).get(AddLocationViewModel.class);
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
            CommunicationUtils.showOneAnswerDialog(AddLocationActivity.this, getResources().getString(R.string.add_location_label_empty), okText, true, Dialog::dismiss);
        }
        // then check if the label already exists in the DB
        else {
            addLocationViewModel.getMyLocationWithLabel(label).observe(this, myLocation -> {
                if (myLocation != null) {
                    CommunicationUtils.showOneAnswerDialog(AddLocationActivity.this, getResources().getString(R.string.add_location_label_already_exists), okText, true, Dialog::dismiss);
                }
                // then check that the address is not empty
                else if (address.length() == 0) {
                    CommunicationUtils.showOneAnswerDialog(AddLocationActivity.this, getResources().getString(R.string.add_location_address_empty), okText, true, Dialog::dismiss);
                }
                // then check the latitude and longitude - to have the proper format
                else if (!LocationUtils.hasLatitudePattern(latitude)) {
                    CommunicationUtils.showOneAnswerDialog(AddLocationActivity.this, getResources().getString(R.string.add_location_latitude_invalid), okText, true, Dialog::dismiss);
                } else if (!LocationUtils.hasLongitudePattern(longitude)) {
                    CommunicationUtils.showOneAnswerDialog(AddLocationActivity.this, getResources().getString(R.string.add_location_longitude_invalid), okText, true, Dialog::dismiss);
                } else {
                    // after all validations, insert in DB
                    addLocationViewModel.insertMyLocationIntoDb(new MyLocation(label, address, Double.parseDouble(latitude), Double.parseDouble(longitude)));
                    this.onAddLocationSuccessful();
                }
            });
        }
    }
    //endregion

    //region Navigator
    @Override
    public void onAddLocationSuccessful() {
        finish();
    }
    //endregion
}
