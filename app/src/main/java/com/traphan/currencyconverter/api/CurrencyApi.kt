package com.traphan.currencyconverter.api

import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import io.reactivex.Observable
import retrofit2.http.GET

interface CurrencyApi {

    @GET("daily_json.js")
    fun fetchCurrency() : Observable<CurrencyResponseRoot>
}