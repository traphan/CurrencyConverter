package traphan.ren95.convertcurrency.di.module

import traphan.ren95.convertcurrency.ui.activity.Splash
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class, ServiceModule::class])
    abstract fun contributeSplash(): Splash
}