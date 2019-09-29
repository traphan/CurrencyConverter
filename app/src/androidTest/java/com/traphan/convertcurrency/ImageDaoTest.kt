package com.traphan.convertcurrency

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import traphan.ren95.convertcurrency.database.AppDatabase
import traphan.ren95.convertcurrency.database.dao.ImageDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class ImageDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var imageDao: ImageDao

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        imageDao = appDatabase.imageDao()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun testImageDao() {
        val size = Util.getRandomNumberInRange(15, 50)
        val randomIndex = Util.getRandomNumberInRange(0, 14)
        val imageEntities = ImageDaoTestHelper().getImageEntities(size)
        imageDao.insertOrUpdateAllImage(imageEntities).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            val countObserver = imageDao.getCount().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test()
            countObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
            countObserver.assertNoErrors().assertComplete()
            Assert.assertEquals(countObserver.values()[0][0], size)
            countObserver.dispose()
            val loadImagesObserver = imageDao.loadAllImages().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test()
            loadImagesObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
            loadImagesObserver.assertNoErrors().assertComplete().assertValueCount(imageEntities.size).assertValue(imageEntities)
            loadImagesObserver.dispose()
            val loadImageObserver = imageDao.loadImage(imageEntities[randomIndex].currencyRemoteId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).test()
            loadImageObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
            loadImageObserver.assertNoErrors().assertComplete().assertValueCount(1).assertValue(imageEntities[randomIndex])
            loadImageObserver.dispose()
            val deleteObserver = imageDao.deleteAllImage().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test()
            deleteObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
            deleteObserver.assertNoErrors().assertComplete()
            deleteObserver.dispose()
        }
    }
}