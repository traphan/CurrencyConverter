package com.traphan.currencyconverter.ui.viewmodel

import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.ui.base.BaseViewModel
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.repository.CurrencyRepository
import com.traphan.currencyconverter.repository.CurrencyRepositoryImpl
import io.reactivex.Observable
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(currencyApi: CurrencyApi, currencyDao: CurrencyDao ): BaseViewModel() {

    lateinit var currencyRepository: CurrencyRepository

    init {
        currencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi)
    }

    fun getAllCurrency(): Observable<List<CurrencyEntity>> {
        return currencyRepository.getAllCurrency()
    }
}