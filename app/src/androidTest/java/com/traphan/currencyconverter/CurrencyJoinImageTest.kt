package com.traphan.currencyconverter

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.traphan.currencyconverter.database.AppDatabase
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.database.entity.ImageEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class CurrencyJoinImageTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var currencyDao: CurrencyDao
    private lateinit var imageDao: ImageDao

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        currencyDao = appDatabase.currencyDao()
        imageDao = appDatabase.imageDao()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun testCurrencyJoinImageTest() {
        val size = Util.getRandomNumberInRange(5, 50)
        var currenciesImage = CurrencyJoinImageTestHelper().getCurrenciesJoinImage(size)
        var currencies: List<CurrencyEntity> = listOf()
        var images: List<ImageEntity> = listOf()
        var ids: List<String> = listOf()
        currenciesImage.forEach{
            ids = ids.plus(it.currencyEntity.idRemote)
            currencies = currencies.plus(it.currencyEntity)
            images = images.plus(it.imageEntity)
        }
        currencyDao.insertOrUpdateAllCurrency(currencies).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            imageDao.insertOrUpdateAllImage(images).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                val currencyImageObserver = currencyDao.loadAllCurrencyJoinImage().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).test()
                currencyImageObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
                currencyImageObserver.assertNoErrors().assertComplete().assertValueCount(size).assertValue(currenciesImage)
                currencyImageObserver.dispose()
                ids = ids.minus(currencies[4].idRemote)
                currenciesImage = currenciesImage.minus(currenciesImage[4])
                val currencyIdsImageObserver = currencyDao.loadCurrencyJoinImage(ids).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).test()
                currencyIdsImageObserver.assertValueCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
                currencyIdsImageObserver.assertNoErrors().assertComplete().assertValueCount(currenciesImage.size).assertValue(currenciesImage)
                currencyIdsImageObserver.dispose()
            }
        }

    }
}