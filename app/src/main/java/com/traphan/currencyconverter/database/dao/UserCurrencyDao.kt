package com.traphan.currencyconverter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.traphan.currencyconverter.database.entity.UserCurrency
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface UserCurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUserCurrency(userCurrencies: List<UserCurrency>): Completable

    @Query("DELETE from user_currency")
    fun deleteAll(): Completable

    @Query("SELECT * FROM user_currency")
    fun loadAllUserCurrency(): Observable<List<UserCurrency>>

    @Query("SELECT COUNT(1) FROM user_currency")
    fun getCount() : Observable<List<Int>>
}