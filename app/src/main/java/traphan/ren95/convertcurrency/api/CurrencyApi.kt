package traphan.ren95.convertcurrency.api

import traphan.ren95.convertcurrency.api.entity.CurrencyResponseRoot
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface CurrencyApi {

    @GET("daily_json.js")
    fun fetchCurrency() : Observable<CurrencyResponseRoot>

    @GET
    fun fetchImages(@Url url: String): Observable<ResponseBody>
}