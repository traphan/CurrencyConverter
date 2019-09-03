package com.traphan.currencyconverter.di.module

import com.traphan.currencyconverter.ui.BaseCurrencyFragment
import com.traphan.currencyconverter.ui.CurrencyCalculationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseCurrencyFragment(): BaseCurrencyFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrencyCalculationFragment(): CurrencyCalculationFragment
}