package traphan.ren95.convertcurrency.api

import traphan.ren95.convertcurrency.api.entity.CurrencyResponseRoot
import io.reactivex.Observable
import okhttp3.ResponseBody

class CurrencyRemoteImpl constructor(private val currencyApi: CurrencyApi):
    CurrencyRemote {
    override fun fetchImages(url: String): Observable<ResponseBody> {
        return currencyApi.fetchImages(url)
    }


    override fun fetchCurrency(): Observable<CurrencyResponseRoot> {
        return currencyApi.fetchCurrency()
    }
}