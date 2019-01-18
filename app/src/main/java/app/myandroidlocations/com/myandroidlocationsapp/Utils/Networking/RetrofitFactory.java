package app.myandroidlocations.com.myandroidlocationsapp.Utils.Networking;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static Retrofit client = null;

    public static Retrofit getInstance(String baseUrl) {
        if (client == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return client;
    }
}
