package com.traphan.currencyconverter
import android.app.Application
import com.traphan.currencyconverter.di.component.DaggerAppComponent
import com.traphan.currencyconverter.di.module.ApiModule
import com.traphan.currencyconverter.di.module.DbModule

class AppApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .apiModule(ApiModule())
            .dbModule(DbModule())
            .build()
            .inject(this)
    }
}