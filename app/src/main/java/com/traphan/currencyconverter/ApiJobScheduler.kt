package com.traphan.currencyconverter

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.CurrencyRemote
import com.traphan.currencyconverter.api.CurrencyRemoteImpl
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.dao.UserCurrencyDao
import com.traphan.currencyconverter.database.datasource.CurrencyLocal
import com.traphan.currencyconverter.database.datasource.ImageLocal
import com.traphan.currencyconverter.database.datasource.UserCurrencyLocal
import com.traphan.currencyconverter.database.datasourceimpl.CurrencyLocalImpl
import com.traphan.currencyconverter.database.datasourceimpl.ImageLocalImpl
import com.traphan.currencyconverter.database.datasourceimpl.UserCurrencyLocalImpl
import com.traphan.currencyconverter.repository.converter.CurrencyRemoteToLocalConverter
import com.traphan.currencyconverter.repository.unzip.unpackZip
import dagger.android.AndroidInjection
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ApiJobScheduler : JobService() {


    @Inject
    lateinit var userCurrencyDao: UserCurrencyDao
    @Inject
    lateinit var currencyDao: CurrencyDao
    @Inject
    lateinit var currencyApi: CurrencyApi
    @Inject
    lateinit var imageDao: ImageDao

    private lateinit var currencyRemote: CurrencyRemote
    private lateinit var currencyLocal: CurrencyLocal
    private lateinit var imageLocal: ImageLocal
    private lateinit var userCurrencyLocal:UserCurrencyLocal
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        currencyRemote = CurrencyRemoteImpl(currencyApi)
        currencyLocal = CurrencyLocalImpl(currencyDao)
        imageLocal = ImageLocalImpl(imageDao)
        userCurrencyLocal = UserCurrencyLocalImpl(userCurrencyDao)

    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        compositeDisposable.clear()
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        if(isInternetAvailable()) {
            compositeDisposable.add(
                fetchCurrency().subscribeOn(Schedulers.io()).subscribe(
                    {}, {})
            )
            return true
        } else {
            return false
        }
    }

    private fun fetchCurrency(): Completable {
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
        compositeDisposable.add(currencyRemote.fetchImages("https://drive.google.com/uc?id=1-swLg3qum_73lJ1-G3ysQDsTx46OItNw").subscribeOn(
            Schedulers.io()).subscribeOn(Schedulers.io()).subscribe{
            unpackZip(it.byteStream(), baseContext).subscribeOn(Schedulers.io()).subscribe{ imageEntity ->
                imageLocal.insertOrUpdateAllImage(imageEntity).subscribeOn(Schedulers.io()).subscribe({}, {})
            }
        })
    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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