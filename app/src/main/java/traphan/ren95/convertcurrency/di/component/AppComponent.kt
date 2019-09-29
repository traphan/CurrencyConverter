package traphan.ren95.convertcurrency.di.component

import android.app.Application
import traphan.ren95.convertcurrency.AppApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import traphan.ren95.convertcurrency.di.module.*
import traphan.ren95.convertcurrency.di.module.ViewModelModule
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