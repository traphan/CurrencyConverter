package com.traphan.currencyconverter.di

import android.app.Application
import com.traphan.currencyconverter.AppApplication
import com.traphan.currencyconverter.api.ApiModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApiModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule): Builder

        fun build(): AppComponent
    }

    fun inject(appApplication: AppApplication)
}