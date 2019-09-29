package traphan.ren95.convertcurrency.database.datasource

import traphan.ren95.convertcurrency.database.entity.UserCurrency
import io.reactivex.Completable
import io.reactivex.Observable

interface UserCurrencyLocal {

    fun insertAllUserCurrency(userCurrencies: List<UserCurrency>): Completable

    fun deleteAll(): Completable

    fun loadAllUserCurrency(): Observable<List<UserCurrency>>

    fun getCount() : Observable<List<Int>>
}