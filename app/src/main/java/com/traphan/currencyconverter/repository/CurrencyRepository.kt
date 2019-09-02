package com.traphan.currencyconverter.repository

import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.UserCurrency
import io.reactivex.Completable
import io.reactivex.Observable

interface CurrencyRepository {

    fun fetchCurrency(): Completable

    fun getAllCurrency(): Observable<List<CurrencyEntity>>

    fun loadAllCurrencyJoin(): Observable<List<CurrencyJoinImage>>

    fun insertAllUserCurrency(userCurrencies: List<UserCurrency>): Completable

    fun loadAllUserCurrency(): Observable<List<UserCurrency>>

    fun getCountUserCurrency(): Observable<List<Int>>
}