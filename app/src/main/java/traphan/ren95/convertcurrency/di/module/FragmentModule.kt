package traphan.ren95.convertcurrency.di.module

import traphan.ren95.convertcurrency.ui.fragment.BaseCurrencyFragment
import traphan.ren95.convertcurrency.ui.fragment.CurrencyCalculationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseCurrencyFragment(): BaseCurrencyFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrencyCalculationFragment(): CurrencyCalculationFragment
}