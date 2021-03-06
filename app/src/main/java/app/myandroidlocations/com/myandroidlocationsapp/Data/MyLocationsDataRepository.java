package app.myandroidlocations.com.myandroidlocationsapp.Data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import app.myandroidlocations.com.myandroidlocationsapp.Utils.NetworkingUtils.RetrofitFactory;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.SharedPrefUtils.SharedPref;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyLocationsDataRepository {
    private MyLocationDao myLocationsDao;
    private Context context;
    private boolean isTheFirstRunOfTheApp = false;
    private final String SHARED_PREF_IS_THE_FIRST_RUN = "SharedPrefIsTheFirstRun";
    private Disposable getListOfLocationsDisposable;
    private MyLocationsApiInterface myLocationsService;
    private final String TAG = this.getClass().getName();

    //region Constructor
    public MyLocationsDataRepository(Context context) {
        this.context = context;
        initializeMyLocationsDao();
        checkIfAppHasRunForTheFirstTime();
    }
    //endregion

    //region Populate database with my locations from the api (just on the first run)
    private void checkIfAppHasRunForTheFirstTime() {
        isTheFirstRunOfTheApp = SharedPref.getInstance(context).getString(SHARED_PREF_IS_THE_FIRST_RUN) != null;
        if (!isTheFirstRunOfTheApp) {
            SharedPref.getInstance(context).store(SHARED_PREF_IS_THE_FIRST_RUN, SHARED_PREF_IS_THE_FIRST_RUN);
            getInitialDataFromApi();
        }
    }
    //endregion

    //region API calls
    private void getInitialDataFromApi() {
        myLocationsService = RetrofitFactory.getInstance(Constants.API_SERVER_BASE_URL).create(MyLocationsApiInterface.class);
        getListOfLocationsDisposable = myLocationsService.getAllMyLocations().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                this::insertIntoDb,
                error -> Log.d(TAG, "error on loading the data from api"));
    }
    //endregion

    //region LiveData objects / getters
    public LiveData<List<MyLocation>> getAllMyLocations() {
        return myLocationsDao.getAllMyLocations();
    }

    public LiveData<MyLocation> getMyLocationWithId(int id) {
        return myLocationsDao.getMyLocationWithId(id);
    }

    public LiveData<MyLocation> getMyLocationWithLabel(String label) {
        return myLocationsDao.getMyLocationWithLabel(label);
    }
    //endregion

    //region Database
    private void initializeMyLocationsDao() {
        myLocationsDao = MyLocationsDb.getDatabase(context).myLocationDao();
    }

    public void insertIntoDb(MyLocation myLocation) {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.insert(myLocation));
    }

    public void updateLocationInDb(MyLocation myLocation) {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.update(myLocation));
    }

    void insertIntoDb(List<MyLocation> myLocations) {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.insert(myLocations));
    }

    public void deleteLocationFromDb(MyLocation myLocation) {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.deleteMyLocation(myLocation));
    }

    void clearAllDb() {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.deleteAll()); // do not erase this methods, will be useful in the future
    }
    //endregion

    //region Clear rxJava disposables
    public void clearDisposables() {
        if (getListOfLocationsDisposable != null && !getListOfLocationsDisposable.isDisposed()) {
            getListOfLocationsDisposable.dispose();
        }
    }
    //endregion
}
