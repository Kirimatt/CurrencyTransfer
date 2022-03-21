package kirimatt.currencytransfer;

import android.app.Application;

import com.google.gson.Gson;

import kirimatt.currencytransfer.daos.JsonDao;
import kirimatt.currencytransfer.interfaces.CurrencyApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyApp extends Application {

    public static final Gson GSON = new Gson();
    private static CurrencyApi currencyApi;
    private static JsonDao jsonDao;

    public static CurrencyApi getCurrencyApi() {
        return currencyApi;
    }

    public static void setCurrencyApi(CurrencyApi currencyApi) {
        CurrencyApp.currencyApi = currencyApi;
    }

    public static JsonDao getJsonDao() {
        return jsonDao;
    }

    public static void setJsonDao(JsonDao jsonDao) {
        CurrencyApp.jsonDao = jsonDao;
    }

    public static Gson getGSON() {
        return GSON;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        configureRetrofit();
    }

    private void configureRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.cbrLink))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        setCurrencyApi(retrofit.create(CurrencyApi.class));
    }
}
