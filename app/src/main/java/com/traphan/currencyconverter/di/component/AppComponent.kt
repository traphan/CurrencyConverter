package com.traphan.currencyconverter.di.component

import android.app.Application
import com.traphan.currencyconverter.AppApplication
import com.traphan.currencyconverter.di.module.*
import com.traphan.currencyconverter.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [ApiModule::class, DbModule::class, ViewModelModule::class, ActivityModule::class, AndroidSupportInjectionModule::class, FragmentModule::class,
    ServiceModule::class])
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