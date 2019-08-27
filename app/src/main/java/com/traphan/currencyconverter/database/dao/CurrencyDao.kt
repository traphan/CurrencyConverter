package com.traphan.currencyconverter.database.dao

import androidx.room.*
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import io.reactivex.Completable
import io.reactivex.Observable

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
}