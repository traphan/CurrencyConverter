package com.traphan.currencyconverter.api

import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.CurrencyRemote
import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import io.reactivex.Observable

class CurrencyRemoteImpl constructor(private val currencyApi: CurrencyApi): CurrencyRemote {


    override fun fetchCurrency(): Observable<CurrencyResponseRoot> {
        return currencyApi.fetchCurrency()
    }
}