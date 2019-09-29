package traphan.ren95.convertcurrency.api

import traphan.ren95.convertcurrency.api.entity.CurrencyResponseRoot
import io.reactivex.Observable
import okhttp3.ResponseBody

interface CurrencyRemote {

    fun fetchCurrency(): Observable<CurrencyResponseRoot>

    fun fetchImages(url: String): Observable<ResponseBody>
}