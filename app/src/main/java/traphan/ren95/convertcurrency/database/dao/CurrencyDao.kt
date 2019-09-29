package traphan.ren95.convertcurrency.database.dao

import androidx.room.*
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import traphan.ren95.convertcurrency.database.entity.CurrencyJoinImage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAllCurrency(currencies : List<CurrencyEntity>) : Completable

    @Query("DELETE FROM currency")
    fun deleteAll() : Completable

    @Query("SELECT * FROM currency")
    fun getAllCurrency() : Observable<List<CurrencyEntity>>

    @Query("SELECT COUNT(1) FROM currency")
    fun getCount() : Observable<List<Int>>

    @Transaction
    @Query("SELECT * FROM currency")
    fun loadAllCurrencyJoinImage(): Observable<List<CurrencyJoinImage>>

    @Transaction
    @Query("SELECT * FROM currency WHERE id_remote IN (:currencyIds)")
    fun loadCurrencyJoinImage(currencyIds: List<String>): Single<List<CurrencyJoinImage>>
}