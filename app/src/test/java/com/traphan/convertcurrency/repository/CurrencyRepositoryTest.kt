package com.traphan.convertcurrency.repository

import android.content.Context
import traphan.ren95.convertcurrency.api.CurrencyApi
import com.traphan.convertcurrency.api.Util
import traphan.ren95.convertcurrency.api.entity.CurrencyResponseRoot
import com.traphan.convertcurrency.api.testhelper.CurrencyApiTestHelper
import traphan.ren95.convertcurrency.database.dao.CurrencyDao
import traphan.ren95.convertcurrency.database.dao.ImageDao
import traphan.ren95.convertcurrency.database.dao.UserCurrencyDao
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import traphan.ren95.convertcurrency.database.entity.CurrencyJoinImage
import traphan.ren95.convertcurrency.database.entity.UserCurrency
import traphan.ren95.convertcurrency.repository.converter.CurrencyRemoteToLocalConverter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import traphan.ren95.convertcurrency.repository.CurrencyRepository
import traphan.ren95.convertcurrency.repository.CurrencyRepositoryImpl
import java.util.concurrent.TimeUnit


class CurrencyRepositoryTest  {

    private lateinit var currencyRepository: CurrencyRepository

    @Mock
    lateinit var userCurrencyDao: UserCurrencyDao
    @Mock
    lateinit var currencyDao: CurrencyDao
    @Mock
    lateinit var currencyApi: CurrencyApi
    @Mock
    lateinit var imageDao: ImageDao

    private lateinit var currencyResponseRoot: CurrencyResponseRoot
    private lateinit var currencies: List<CurrencyEntity>
    private lateinit var currencyJoinImage: List<CurrencyJoinImage>

    private lateinit var schedulers: Scheduler
    private var context = mock(Context::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        currencyRepository = CurrencyRepositoryImpl(
            currencyDao,
            currencyApi,
            userCurrencyDao,
            imageDao,
            context
        )
        currencyResponseRoot = CurrencyApiTestHelper(Util(),
            "C:/Users/traph/AndroidStudioProjects/CurrencyConverter/app/src/test/java/com/traphan/convertcurrency/api/resources/currency").getCurrencyResponse()
        currencies = CurrencyRemoteToLocalConverter(
            currencyResponseRoot
        ).convertAll()
        schedulers = Schedulers.newThread()
        currencyJoinImage = CurrencyApiTestHelper(Util(), "").getCurrencyJoinImage(currencies)
    }

    @Test
    fun loadAllCurrencyJoinTest() {
        `when`(currencyDao.loadAllCurrencyJoinImage()).thenReturn(Observable.just(currencyJoinImage))
        var loadAllCurrencyJoinImageSubscribe = currencyRepository.loadAllCurrencyJoin().subscribeOn(schedulers).test()
        loadAllCurrencyJoinImageSubscribe.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        loadAllCurrencyJoinImageSubscribe.assertNoErrors().assertValue(currencyJoinImage)
        loadAllCurrencyJoinImageSubscribe.dispose()
    }

    @Test
    fun insertAllUserCurrencyTest() {
        val size = Util().getRandomNumberInRange(10, 30)
        val userCurrencies: List<UserCurrency> = UserCurrencyDaoTestHelper().getUserCurrencies(size)
        `when`(userCurrencyDao.deleteAll()).thenReturn(Completable.complete())
        `when`(userCurrencyDao.insertAllUserCurrency(userCurrencies)).thenReturn(Completable.complete())
        var insertAllUserCurrencySubscriber = currencyRepository.insertAllUserCurrency(userCurrencies).subscribeOn(schedulers).test()
        insertAllUserCurrencySubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        insertAllUserCurrencySubscriber.assertNoErrors().assertComplete()
        insertAllUserCurrencySubscriber.dispose()
    }

    @Test
    fun loadAllUserCurrencyTest() {
        val size = Util().getRandomNumberInRange(10, 30)
        val userCurrencies: List<UserCurrency> = UserCurrencyDaoTestHelper().getUserCurrencies(size)
        `when`(userCurrencyDao.loadAllUserCurrency()).thenReturn(Observable.just(userCurrencies))
        var loadAllUserCurrencySubscriber = currencyRepository.loadAllUserCurrency().subscribeOn(schedulers).test()
        loadAllUserCurrencySubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        loadAllUserCurrencySubscriber.assertNoErrors().assertComplete().assertValue(userCurrencies)
        loadAllUserCurrencySubscriber.dispose()
        `when`(userCurrencyDao.loadAllUserCurrency()).thenReturn(Observable.just(listOf()))
        var loadAllUserCurrencySubscriberSecond = currencyRepository.loadAllUserCurrency().subscribeOn(schedulers).test()
        loadAllUserCurrencySubscriberSecond.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        loadAllUserCurrencySubscriberSecond.assertNoErrors().assertComplete()
        Assert.assertEquals(0, loadAllUserCurrencySubscriberSecond.values()[0].size)
        loadAllUserCurrencySubscriberSecond.dispose()
    }

    @Test
    fun getCountUserCurrencyTest() {
        val size = Util().getRandomNumberInRange(0, 30)
        `when`(userCurrencyDao.getCount()).thenReturn(Observable.just(listOf(size)))
        var getUserCountSubscriber = currencyRepository.getCountUserCurrency().subscribeOn(schedulers).test()
        getUserCountSubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        getUserCountSubscriber.assertNoErrors().assertComplete().assertValue(listOf(size))
        getUserCountSubscriber.dispose()
    }

    @Test
    fun loadAllCurrencyJoinIdsTest() {
        var currencyIds: List<String> = listOf()
        var currencyJoinImageOutput: List<CurrencyJoinImage> = listOf()
        val size: Int = Util().getRandomNumberInRange(1, 30)
        for(i: Int in 0..size) {
            currencyIds = currencyIds.plus(currencyJoinImage[i].currencyEntity.idRemote)
            currencyJoinImageOutput = currencyJoinImageOutput.plus(currencyJoinImage[i])
        }
        `when`(currencyDao.loadCurrencyJoinImage(currencyIds)).thenReturn(Single.just(currencyJoinImageOutput))
        var loadAllCurrencyJoinIdsSubscriber = currencyRepository.loadAllCurrencyJoin(currencyIds).subscribeOn(schedulers).test()
        loadAllCurrencyJoinIdsSubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        loadAllCurrencyJoinIdsSubscriber.assertNoErrors().assertComplete().assertValue(currencyJoinImageOutput)
        loadAllCurrencyJoinIdsSubscriber.dispose()
    }

//    @Test
//    fun fetchCurrencyTest() {
//        var response: ResponseBody = mock(ResponseBody::class.java)
//        var inputStream: InputStream = mock(InputStream::class.java)
//        var imageEntity: List<ImageEntity> = listOf()
//        `when`(currencyApi.fetchCurrency()).thenReturn(Observable.just(currencyResponseRoot))
//        `when`(currencyApi.fetchImages("https://drive.google.com/uc?id=1-swLg3qum_73lJ1-G3ysQDsTx46OItNw")).thenReturn(Observable.just(response))
//        `when`(currencyDao.insertOrUpdateAllCurrency(currencies)).thenReturn(Completable.complete())
//        `when`(unpackZip(inputStream, context)).thenReturn(Single.just(imageEntity))
//        var fetchSubscriber = currencyRepository.fetchCurrency().subscribeOn(schedulers).test()
//        fetchSubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
//        fetchSubscriber.assertNoErrors().assertComplete()
//        fetchSubscriber.dispose()
//    }

}