package com.traphan.currencyconverter

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
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
import com.traphan.currencyconverter.repository.CurrencyRepository
import com.traphan.currencyconverter.repository.CurrencyRepositoryImpl
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

    private lateinit var currencyRepository: CurrencyRepository
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        currencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi, userCurrencyDao, imageDao, applicationContext)
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        compositeDisposable.clear()
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        if(currencyRepository.isInternetAvailable()) {
            compositeDisposable.add(
                currencyRepository.fetchCurrency().subscribeOn(Schedulers.io()).subscribe(
                    { Log.d("1", "fetchCurrency success")}, {Log.d("1", it.message.toString())})
            )
            return true
        } else {
            return false
        }
    }
}