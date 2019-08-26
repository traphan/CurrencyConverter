package com.traphan.currencyconverter.api

import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import io.reactivex.Observable
import okhttp3.ResponseBody

interface CurrencyRemote {

    fun fetchCurrency(): Observable<CurrencyResponseRoot>

    fun fetchImages(url: String): Observable<ResponseBody>
}