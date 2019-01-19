package app.myandroidlocations.com.myandroidlocationsapp;

import android.content.Context;

import app.myandroidlocations.com.myandroidlocationsapp.Data.MyLocationsDataRepository;

public class DependencyInjection {
    private static MyLocationsDataRepository myLocationsDataRepository;
    private static DependencyInjection injection;

    public static DependencyInjection getInjector(Context context) {
        if (injection == null) {
            injection = new DependencyInjection();
            initiateMyLocationsDataRepo(context);
        }
        return injection;
    }

    private static void initiateMyLocationsDataRepo(Context context) {
        myLocationsDataRepository = new MyLocationsDataRepository(context);
    }

    public MyLocationsDataRepository injectMyLocationsDataRepo() {
        return myLocationsDataRepository;
    }
}
