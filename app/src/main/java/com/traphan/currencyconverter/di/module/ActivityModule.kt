package com.traphan.currencyconverter.di.module

import com.traphan.currencyconverter.ui.MainActivity
import com.traphan.currencyconverter.ui.Splash
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeSplash(): Splash

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}