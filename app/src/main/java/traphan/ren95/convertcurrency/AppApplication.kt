package traphan.ren95.convertcurrency
import android.app.Activity
import android.app.Application
import android.app.Service
import traphan.ren95.convertcurrency.di.module.ApiModule
import traphan.ren95.convertcurrency.di.module.DbModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import traphan.ren95.convertcurrency.di.component.DaggerAppComponent
import javax.inject.Inject

class AppApplication : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>


    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceInjector
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .apiModule(ApiModule())
            .dbModule(DbModule())
            .build()
            .inject(this)
    }
}