package com.traphan.currencyconverter.repository

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
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
import com.traphan.currencyconverter.ApiJobScheduler


class CurrencyRepositoryImpl constructor(private val currencyDao: CurrencyDao, private val userCurrencyDao: UserCurrencyDao, private val context: Context): CurrencyRepository {

    private val currencyLocal: CurrencyLocal = CurrencyLocalImpl(currencyDao)
    private val userCurrencyLocal = UserCurrencyLocalImpl(userCurrencyDao)
    private val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
    private val componentName:ComponentName = ComponentName(context, ApiJobScheduler::class.java)
    private val jobInfo:JobInfo = JobInfo.Builder(1, componentName).setOverrideDeadline(0).build()

    init {
        jobScheduler.schedule(jobInfo)
    }

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

    override fun loadAllCurrencyJoin(ids: List<String>): Observable<List<CurrencyJoinImage>> {
        return currencyLocal.loadCurrencyJoinImage(ids)
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