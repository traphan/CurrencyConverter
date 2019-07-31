package com.traphan.currencyconverter.di.component

import android.app.Application
import com.traphan.currencyconverter.AppApplication
import com.traphan.currencyconverter.di.module.ApiModule
import com.traphan.currencyconverter.di.module.DbModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApiModule::class, DbModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule) : Builder

        @BindsInstance
        fun dbModule(dbModule: DbModule) : Builder

        fun build(): AppComponent
    }

    fun inject(appApplication: AppApplication)
}