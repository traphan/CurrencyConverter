package com.traphan.currencyconverter.repository

import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.UserCurrency
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