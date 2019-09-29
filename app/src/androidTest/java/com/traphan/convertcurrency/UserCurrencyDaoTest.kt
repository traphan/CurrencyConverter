package com.traphan.convertcurrency

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import traphan.ren95.convertcurrency.database.AppDatabase
import traphan.ren95.convertcurrency.database.dao.UserCurrencyDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class UserCurrencyDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var userCurrencyDao: UserCurrencyDao

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        userCurrencyDao = appDatabase.userCurrencyDao()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun userCurrencyDaoTest() {
        val size = Util.getRandomNumberInRange(5, 50)
        val userCurrencies = UserCurrencyDaoTestHelper().getUserCurrencies(size)
        userCurrencyDao.insertAllUserCurrency(userCurrencies).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            val countObserver = userCurrencyDao.getCount().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test()
            countObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
            countObserver.assertNoErrors().assertComplete()
            Assert.assertEquals(size, countObserver.values()[0][0])
            countObserver.dispose()
            val currenciesObserver = userCurrencyDao.loadAllUserCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test()
            currenciesObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
            currenciesObserver.assertNoErrors().assertComplete().assertValueCount(size).assertValue(userCurrencies)
            currenciesObserver.dispose()
            val deleteObserver = userCurrencyDao.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test()
            deleteObserver.awaitCount(1).awaitDone(3,TimeUnit.MILLISECONDS)
            deleteObserver.assertNoErrors().assertComplete()
            deleteObserver.dispose()
        }
    }
}