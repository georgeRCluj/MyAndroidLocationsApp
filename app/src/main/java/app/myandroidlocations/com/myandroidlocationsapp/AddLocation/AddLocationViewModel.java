package app.myandroidlocations.com.myandroidlocationsapp.AddLocation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocationsDataRepository;
import app.myandroidlocations.com.myandroidlocationsapp.DependencyInjection;
import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocation;

public class AddLocationViewModel extends AndroidViewModel {
    private Context context;
    private MyLocationsDataRepository myLocationsData;

    public AddLocationViewModel(@NonNull Application application) {
        super(application);
        context = application;
        myLocationsData = DependencyInjection.getInjector(context).injectMyLocationsDataRepo();
    }

    LiveData<MyLocation> getMyLocationWithLabel(String label) {
        return myLocationsData.getMyLocationWithLabel(label);
    }

    void insertMyLocationIntoDb(MyLocation myLocation) {
        myLocationsData.insertIntoDb(myLocation);
    }
}
