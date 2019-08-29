package com.traphan.currencyconverter.repository

import android.annotation.SuppressLint
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
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.ImageEntity
import com.traphan.currencyconverter.repository.converter.CurrencyRemoteToLocalConverter
import com.traphan.currencyconverter.repository.unzip.unpackZip
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    @SuppressLint("CheckResult")
    private fun fetchImage() {
        currencyRemote.fetchImages("https://drive.google.com/uc?id=16fHQOOKnv0kStSIy420MGNqF-hkIj6qr").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            unpackZip(it.byteStream()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{patchs ->
                run {
                    var imagesEntity = listOf<ImageEntity>()
                    imagesEntity = imagesEntity.plus(ImageEntity("USD", patchs["USD"]!!))
                    imageDao.insertOrUpdateAllImage(imagesEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{run{Log.d("1", "complete")}
                    }
                }
            }
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

    override fun loadAllCurrencyJoin(): Observable<List<CurrencyJoinImage>> {
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
                        .andThen(currencyLocal.loadAllCurrencyJoinImage())
                } else {
                    currencyLocal.loadAllCurrencyJoinImage()
                }
            }
    }
}