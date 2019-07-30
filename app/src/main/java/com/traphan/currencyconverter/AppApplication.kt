package com.traphan.currencyconverter
import android.app.Application
import com.traphan.currencyconverter.api.ApiModule
import com.traphan.currencyconverter.di.DaggerAppComponent

class AppApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .apiModule(ApiModule())
            .build()
            .inject(this)
    }
}