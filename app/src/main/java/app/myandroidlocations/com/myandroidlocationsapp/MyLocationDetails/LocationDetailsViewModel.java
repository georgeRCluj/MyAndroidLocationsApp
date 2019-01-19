package app.myandroidlocations.com.myandroidlocationsapp.MyLocationDetails;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocationsDataRepository;
import app.myandroidlocations.com.myandroidlocationsapp.DependencyInjection;
import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;

class LocationDetailsViewModel extends AndroidViewModel {
    private Context context;
    private MyLocationsDataRepository myLocationsData;

    public LocationDetailsViewModel(@NonNull Application application) {
        super(application);
        context = application;
        myLocationsData = DependencyInjection.getInjector(context).injectMyLocationsDataRepo();
    }

    LiveData<List<MyLocation>> getAllMyLocations() {
        return myLocationsData.getAllMyLocations();
    }

    LiveData<MyLocation> getMyLocationWithId(int id) {
        return myLocationsData.getMyLocationWithId(id);
    }

    void deleteMyLocationFromDb(MyLocation myLocation) {
        myLocationsData.deleteLocationFromDb(myLocation);
    }
}
