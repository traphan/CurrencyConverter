package traphan.ren95.convertcurrency.repository

import traphan.ren95.convertcurrency.database.entity.CurrencyJoinImage
import traphan.ren95.convertcurrency.database.entity.UserCurrency
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface CurrencyRepository {

    fun loadAllCurrencyJoin(): Observable<List<CurrencyJoinImage>>

    fun insertAllUserCurrency(userCurrencies: List<UserCurrency>): Completable

    fun loadAllUserCurrency(): Observable<List<UserCurrency>>

    fun getCountUserCurrency(): Observable<List<Int>>

    fun loadAllCurrencyJoin(ids: List<String>): Single<List<CurrencyJoinImage>>

    fun isInternetAvailable(): Boolean

    fun fetchCurrency(): Completable
}