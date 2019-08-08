package com.traphan.currencyconverter.api

import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import io.reactivex.Observable

interface CurrencyRemote {

    fun fetchCurrency(): Observable<CurrencyResponseRoot>
}