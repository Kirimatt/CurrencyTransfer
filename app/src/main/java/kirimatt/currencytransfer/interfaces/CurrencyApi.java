package kirimatt.currencytransfer.interfaces;

import io.reactivex.rxjava3.core.Observable;
import kirimatt.currencytransfer.daos.JsonDao;
import retrofit2.http.GET;

public interface CurrencyApi {
    @GET("./daily_json.js")
    Observable<JsonDao> getJsonDto();
}
