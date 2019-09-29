package traphan.ren95.convertcurrency.di.module

import traphan.ren95.convertcurrency.ApiJobScheduler
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun ProvideApiJobScheduler(): ApiJobScheduler
}