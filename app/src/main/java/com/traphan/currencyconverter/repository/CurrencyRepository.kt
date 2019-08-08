package com.traphan.currencyconverter.repository

import com.traphan.currencyconverter.database.entity.CurrencyEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface CurrencyRepository {

    fun fetchCurrency(): Completable

    fun getAllCurrency(): Observable<List<CurrencyEntity>>
}