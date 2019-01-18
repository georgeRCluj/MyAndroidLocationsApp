package app.myandroidlocations.com.myandroidlocationsapp.MyLocations;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.http.GET;

interface MyLocationsApiInterface {

    @GET(Constants.API_GET_INITIAL_LIST_OF_LOCATIONS)
    Maybe<List<MyLocation>> getAllMyLocations();
}
