package app.myandroidlocations.com.myandroidlocationsapp.Data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import app.myandroidlocations.com.myandroidlocationsapp.MyLocations.MyLocation;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.Networking.RetrofitFactory;
import app.myandroidlocations.com.myandroidlocationsapp.Utils.Storage.LocalStorage;
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

    public MyLocationsDataRepository(Context context) {
        this.context = context;
        initializeMyLocationsDao();
        checkIfAppHasRunForTheFirstTime();
    }

    //region Populate database with my locations from the api (just on the first run)
    private void checkIfAppHasRunForTheFirstTime() {
        isTheFirstRunOfTheApp = LocalStorage.getInstance(context).getString(SHARED_PREF_IS_THE_FIRST_RUN) != null;
        if (!isTheFirstRunOfTheApp) {
            LocalStorage.getInstance(context).store(SHARED_PREF_IS_THE_FIRST_RUN, SHARED_PREF_IS_THE_FIRST_RUN);
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
    //endregion

    //region Database
    private void initializeMyLocationsDao() {
        myLocationsDao = MyLocationsDb.getDatabase(context).myLocationDao();
    }

    void insertIntoDb(MyLocation myLocation) {
//        MyLocation myLocation = new MyLocation("Home", "Floresti, Narciselor 24", -34.5, 151.7);
//        insertIntoDb(myLocation);
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.insert(myLocation));
    }

    void insertIntoDb(List<MyLocation> myLocations) {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.insert(myLocations));
    }

    void clearAllDb() {
        Executors.newSingleThreadExecutor().execute(() -> myLocationsDao.deleteAll());
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
