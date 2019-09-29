package traphan.ren95.convertcurrency.database.datasource

import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import traphan.ren95.convertcurrency.database.entity.CurrencyJoinImage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface CurrencyLocal {

    fun insertOrUpdateAllCurrency(currencies : List<CurrencyEntity>) : Completable

    fun deleteAllCurrency() : Completable

    fun getAllCurrency() : Observable<List<CurrencyEntity>>

    fun getCount() : Observable<List<Int>>

    fun loadAllCurrencyJoinImage(): Observable<List<CurrencyJoinImage>>

    fun loadCurrencyJoinImage(currencyIds: List<String>): Single<List<CurrencyJoinImage>>

}