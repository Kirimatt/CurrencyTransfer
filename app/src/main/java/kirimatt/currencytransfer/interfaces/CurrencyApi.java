package kirimatt.currencytransfer.interfaces;

import kirimatt.currencytransfer.daos.JsonDao;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyApi {
    @GET("./daily_json.js")
    Call<JsonDao> getJsonDto();
}
