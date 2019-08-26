package com.traphan.currencyconverter.repository

import android.util.Log
import com.traphan.currencyconverter.api.CurrencyRemoteImpl
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.CurrencyRemote
import com.traphan.currencyconverter.database.datasource.CurrencyLocal
import com.traphan.currencyconverter.database.datasourceimpl.CurrencyLocalImpl
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.datasource.ImageLocal
import com.traphan.currencyconverter.database.datasourceimpl.ImageLocalImpl
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.repository.converter.CurrencyRemoteToLocalConverter
import com.traphan.currencyconverter.repository.unzip.doZip
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class CurrencyRepositoryImpl constructor(private val currencyDao: CurrencyDao, private val currencyApi: CurrencyApi, private val imageDao: ImageDao): CurrencyRepository {

    private val currencyRemote: CurrencyRemote = CurrencyRemoteImpl(currencyApi)
    private val currencyLocal: CurrencyLocal = CurrencyLocalImpl(currencyDao)
    private val imageLocal: ImageLocal = ImageLocalImpl(imageDao)


    override fun fetchCurrency(): Completable {
        fetchImage()
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

    private fun fetchImage() {
        val patch = "image"
        val file = File(patch)
        currencyRemote.fetchImages("https://drive.google.com/uc?id=1lp5m6xI8LUu89KSI3heWmZh3eTgLNc34").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            doZip(file, ZipOutputStream(it.byteStream()))
            Log.d("1", it.byteStream().toString())
        }
    }

    override fun getAllCurrency(): Observable<List<CurrencyEntity>> {
        fetchImage()
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