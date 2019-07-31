package com.traphan.currencyconverter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAllCurrency(currencies : List<CurrencyEntity>) : Completable

//    @Query("DELETE FROM currency")
//    fun deleteAll() : Completable

    @Query("SELECT * FROM currency")
    fun getAllCurrency() : Observable<List<CurrencyEntity>>

    @Query("SELECT COUNT(1) FROM currency")
    fun getCount() : Observable<List<Int>>
}