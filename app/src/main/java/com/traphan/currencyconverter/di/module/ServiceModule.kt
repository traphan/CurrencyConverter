package com.traphan.currencyconverter.di.module

import com.traphan.currencyconverter.ApiJobScheduler
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun ProvideApiJobScheduler(): ApiJobScheduler
}