package com.traphan.currencyconverter.database

import io.reactivex.Completable
import io.reactivex.Observable

interface CurrencyLocal {

    fun insertOrUpdateAllCurrency(currencies : List<CurrencyEntity>) : Completable

    fun deleteAllCurrency() : Completable

    fun getAllCurrency() : Observable<List<CurrencyEntity>>

    fun getCount() : Observable<List<Int>>
}