package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocations.Data.MyLocationsDataRepository;

public class MyLocationsViewModel extends AndroidViewModel {
    private Context context;
    private MyLocationsDataRepository myLocationsData;

    public MyLocationsViewModel(@NonNull Application application) {
        super(application);
        context = application;
        createMyLocationsDataRepo();
    }

    private void createMyLocationsDataRepo() {
        myLocationsData = new MyLocationsDataRepository(context);
    }

    LiveData<List<MyLocation>> getAllMyLocations() {
        return myLocationsData.getAllMyLocations();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        myLocationsData.clearDisposables();
    }
}
