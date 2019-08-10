package com.traphan.currencyconverter.database

import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import io.reactivex.Completable
import io.reactivex.Observable

class CurrencyLocalImpl(private val currencyDao: CurrencyDao) : CurrencyLocal {

    override fun insertOrUpdateAllCurrency(currencies : List<CurrencyEntity>): Completable {
        return currencyDao.insertOrUpdateAllCurrency(currencies)
    }

    override fun deleteAllCurrency(): Completable {
        return currencyDao.deleteAll()
    }

    override fun getAllCurrency(): Observable<List<CurrencyEntity>> {
        return currencyDao.getAllCurrency()
    }

    override fun getCount(): Observable<List<Int>> {
        return currencyDao.getCount()
    }
}