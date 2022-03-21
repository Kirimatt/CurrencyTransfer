package kirimatt.currencytransfer.services;

import kirimatt.currencytransfer.daos.JsonDao;
import kirimatt.currencytransfer.interfaces.CurrencyApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyService {

    private static final CurrencyApi currencyApi;

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.cbr-xml-daily.ru")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        currencyApi = retrofit.create(CurrencyApi.class);
    }

    private CurrencyService() {
    }

    public static JsonDao getJsonDao() {
        return currencyApi.getJsonDto()
                .onErrorReturn(e -> new JsonDao())
                .blockingFirst();
    }
}
