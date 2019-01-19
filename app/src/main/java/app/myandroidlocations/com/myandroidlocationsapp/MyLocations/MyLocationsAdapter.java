package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.LocationUtils.LocationUtils;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.ActivityMyLocationsItemBinding;

public class MyLocationsAdapter extends RecyclerView.Adapter {
    private List<MyLocation> myLocationsList;
    private MyLocationsNavigator navigator;
    private final int EMPTY_COUNTER = 0;
    private Location myRealLocation;

    //region Constructor
    public MyLocationsAdapter(MyLocationsNavigator navigator) {
        this.navigator = navigator;
    }
    //endregion

    //region Refresh methods
    void refreshMyLocationsList(List<MyLocation> locationList) {
        this.myLocationsList = locationList;
        notifyDataSetChanged();
    }

    void refreshMyCurrentLocation(Location location) {
        myRealLocation = location;
        notifyDataSetChanged();
    }
    //endregion

    //region Create, Bind view holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ActivityMyLocationsItemBinding binding = ActivityMyLocationsItemBinding.inflate(layoutInflater, parent, false);
        return new MyLocationsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyLocationsViewHolder patientViewHolder = (MyLocationsViewHolder) holder;
        patientViewHolder.bind(myLocationsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (myLocationsList != null) {
            return myLocationsList.size();
        }
        return EMPTY_COUNTER;
    }
    //endregion

    //region Self View Holder
    private class MyLocationsViewHolder extends RecyclerView.ViewHolder {
        private ActivityMyLocationsItemBinding binding;

        private MyLocationsViewHolder(ActivityMyLocationsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MyLocation currentMyLocation) {
            binding.setMyLocation(currentMyLocation);
            binding.setAirDistanceToCurrentPosition(LocationUtils.getDistanceToCurrentLocation(currentMyLocation, myRealLocation));
            configureClickOnMyLocation(currentMyLocation);
            binding.executePendingBindings();
        }

        private void configureClickOnMyLocation(MyLocation currentMyLocation) {
            binding.myLocationContainer.setOnClickListener(v -> navigator.goToMyLocationDetails(currentMyLocation.getId()));
        }
    }
    //endregion
}


