package traphan.ren95.convertcurrency

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import traphan.ren95.convertcurrency.api.CurrencyApi
import traphan.ren95.convertcurrency.database.dao.CurrencyDao
import traphan.ren95.convertcurrency.database.dao.ImageDao
import traphan.ren95.convertcurrency.database.dao.UserCurrencyDao
import traphan.ren95.convertcurrency.repository.CurrencyRepository
import traphan.ren95.convertcurrency.repository.CurrencyRepositoryImpl
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ApiJobScheduler : JobService() {


    @Inject
    lateinit var userCurrencyDao: UserCurrencyDao
    @Inject
    lateinit var currencyDao: CurrencyDao
    @Inject
    lateinit var currencyApi: CurrencyApi
    @Inject
    lateinit var imageDao: ImageDao

    private lateinit var currencyRepository: CurrencyRepository
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        currencyRepository = CurrencyRepositoryImpl(
            currencyDao,
            currencyApi,
            userCurrencyDao,
            imageDao,
            applicationContext
        )
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        compositeDisposable.clear()
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        if(currencyRepository.isInternetAvailable()) {
            compositeDisposable.add(
                currencyRepository.fetchCurrency().subscribeOn(Schedulers.io()).subscribe(
                    { Log.d("1", "fetchCurrency success")}, {Log.d("1", it.message.toString())})
            )
            return true
        } else {
            return false
        }
    }
}