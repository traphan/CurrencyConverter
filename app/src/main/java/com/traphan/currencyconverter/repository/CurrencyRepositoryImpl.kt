package com.traphan.currencyconverter.repository

import android.content.Context
import com.traphan.currencyconverter.api.CurrencyRemoteImpl
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.CurrencyRemote
import com.traphan.currencyconverter.database.datasource.CurrencyLocal
import com.traphan.currencyconverter.database.datasourceimpl.CurrencyLocalImpl
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.dao.UserCurrencyDao
import com.traphan.currencyconverter.database.datasource.ImageLocal
import com.traphan.currencyconverter.database.datasourceimpl.ImageLocalImpl
import com.traphan.currencyconverter.database.datasourceimpl.UserCurrencyLocalImpl
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.UserCurrency
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.traphan.currencyconverter.repository.converter.CurrencyRemoteToLocalConverter
import com.traphan.currencyconverter.repository.unzip.unpackZip
import io.reactivex.Single


class CurrencyRepositoryImpl constructor(private val currencyDao: CurrencyDao, private val currencyApi: CurrencyApi,
                                         private val userCurrencyDao: UserCurrencyDao, private val imageDao: ImageDao,
                                         private val context: Context): CurrencyRepository {

    private val currencyLocal: CurrencyLocal = CurrencyLocalImpl(currencyDao)
    private val userCurrencyLocal = UserCurrencyLocalImpl(userCurrencyDao)
    private val currencyRemote: CurrencyRemote = CurrencyRemoteImpl(currencyApi)
    private val imageLocal: ImageLocal = ImageLocalImpl(imageDao)

    override fun loadAllCurrencyJoin(): Observable<List<CurrencyJoinImage>> {
        return currencyLocal.loadAllCurrencyJoinImage()
    }


    override fun insertAllUserCurrency(userCurrencies: List<UserCurrency>): Completable {
        return userCurrencyLocal.deleteAll().subscribeOn(Schedulers.io()).andThen(userCurrencyLocal.insertAllUserCurrency(userCurrencies))
    }


    override fun loadAllUserCurrency(): Observable<List<UserCurrency>> {
        return userCurrencyLocal.loadAllUserCurrency()
    }


    override fun getCountUserCurrency(): Observable<List<Int>> {
        return userCurrencyLocal.getCount()
    }

    override fun loadAllCurrencyJoin(ids: List<String>): Single<List<CurrencyJoinImage>> {
        return currencyLocal.loadCurrencyJoinImage(ids)
    }

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
        currencyRemote.fetchImages("https://drive.google.com/uc?id=1-swLg3qum_73lJ1-G3ysQDsTx46OItNw").subscribeOn(
            Schedulers.io()).subscribeOn(Schedulers.io()).subscribe{
            unpackZip(it.byteStream(), context).subscribeOn(Schedulers.io()).subscribe{ imageEntity ->
                imageLocal.insertOrUpdateAllImage(imageEntity).subscribeOn(Schedulers.io()).subscribe({}, {})
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun isInternetAvailable(): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }
}