package com.traphan.currencyconverter.database.datasourceimpl

import com.traphan.currencyconverter.database.dao.UserCurrencyDao
import com.traphan.currencyconverter.database.datasource.UserCurrencyLocal
import com.traphan.currencyconverter.database.entity.UserCurrency
import io.reactivex.Completable
import io.reactivex.Observable

class UserCurrencyLocalImpl(private val userCurrencyDao: UserCurrencyDao): UserCurrencyLocal {

    override fun getCount(): Observable<List<Int>> {
        return userCurrencyDao.getCount()
    }

    override fun insertAllUserCurrency(userCurrencies: List<UserCurrency>): Completable {
        return userCurrencyDao.insertAllUserCurrency(userCurrencies)
    }

    override fun deleteAll(): Completable {
        return userCurrencyDao.deleteAll()
    }

    override fun loadAllUserCurrency(): Observable<List<UserCurrency>> {
        return userCurrencyDao.loadAllUserCurrency()
    }
}