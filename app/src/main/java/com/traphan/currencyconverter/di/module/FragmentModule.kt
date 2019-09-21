package com.traphan.currencyconverter.di.module

import com.traphan.currencyconverter.ui.fragment.BaseCurrencyFragment
import com.traphan.currencyconverter.ui.fragment.CurrencyCalculationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseCurrencyFragment(): BaseCurrencyFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrencyCalculationFragment(): CurrencyCalculationFragment
}