package com.traphan.currencyconverter
import android.app.Activity
import android.app.Application
import android.app.Service
import com.traphan.currencyconverter.di.component.DaggerAppComponent
import com.traphan.currencyconverter.di.module.ApiModule
import com.traphan.currencyconverter.di.module.DbModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

class AppApplication : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>


    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceInjector
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }


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