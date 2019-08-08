package com.traphan.currencyconverter.repository

import com.traphan.currencyconverter.api.CurrencyRemoteImpl
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.CurrencyRemote
import com.traphan.currencyconverter.database.CurrencyLocal
import com.traphan.currencyconverter.database.CurrencyLocalImpl
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.repository.converter.CurrencyRemoteToLocalConverter
import io.reactivex.Completable
import io.reactivex.Observable

class CurrencyRepositoryImpl constructor(private val currencyDao: CurrencyDao, private val currencyApi: CurrencyApi): CurrencyRepository {

    private var currencyRemote: CurrencyRemote = CurrencyRemoteImpl(currencyApi)
    private var currencyLocal: CurrencyLocal = CurrencyLocalImpl(currencyDao)


    override fun fetchCurrency(): Completable {
        return currencyRemote.fetchCurrency()
            .flatMap { currencyResponseRoot ->
                currencyLocal.insertOrUpdateAllCurrency(
                    CurrencyRemoteToLocalConverter(
                        currencyResponseRoot
                    ).convertAll()
                ).toObservable<Any>()
            }
            .ignoreElements()
    }

    override fun getAllCurrency(): Observable<List<CurrencyEntity>> {
        return currencyLocal.getCount()
            .flatMap {counts ->
                if (counts.isEmpty() || counts[0] == 0) {
                    currencyRemote.fetchCurrency()
                        .flatMap { currencyResponseRoot ->
                            currencyLocal.insertOrUpdateAllCurrency(
                                CurrencyRemoteToLocalConverter(
                                    currencyResponseRoot
                                ).convertAll()
                            ).toObservable<Any>()
                        }
                        .ignoreElements()
                        .andThen(currencyLocal.getAllCurrency())
                } else {
                    currencyLocal.getAllCurrency()
                }
            }
    }
}