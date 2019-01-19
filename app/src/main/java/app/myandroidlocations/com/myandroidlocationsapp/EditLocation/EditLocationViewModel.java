package app.myandroidlocations.com.myandroidlocationsapp.EditLocation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocationsDataRepository;
import app.myandroidlocations.com.myandroidlocationsapp.DependencyInjection;

class EditLocationViewModel extends AndroidViewModel {
    private Context context;
    private MyLocationsDataRepository myLocationsData;

    public EditLocationViewModel(@NonNull Application application) {
        super(application);
        context = application;
        myLocationsData = DependencyInjection.getInjector(context).injectMyLocationsDataRepo();
    }

    LiveData<MyLocation> getMyLocationWithId(int id) {
        return myLocationsData.getMyLocationWithId(id);
    }

    void updateMyLocation(MyLocation myLocation) {
        myLocationsData.updateLocationInDb(myLocation);
    }
}
