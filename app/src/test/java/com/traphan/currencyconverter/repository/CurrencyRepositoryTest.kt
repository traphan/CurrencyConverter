package com.traphan.currencyconverter.repository

import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.Util
import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import com.traphan.currencyconverter.api.testhelper.CurrencyApiTestHelper
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.repository.converter.CurrencyRemoteToLocalConverter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class CurrencyRepositoryTest  {

    private lateinit var currencyRepository: CurrencyRepository

    @Mock
    lateinit var currencyApi: CurrencyApi

    @Mock
    lateinit var currencyDao: CurrencyDao

    private lateinit var currencyResponseRoot: CurrencyResponseRoot
    private lateinit var currencies: List<CurrencyEntity>
    private val emptyList = arrayListOf<Int>()
    private lateinit var schedulers: Scheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        currencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi)
        currencyResponseRoot = CurrencyApiTestHelper(Util(),
            "C:/Users/traph/AndroidStudioProjects/CurrencyConverter/app/src/test/java/com/traphan/currencyconverter/api/resources/currency").getCurrencyResponse()
        currencies = CurrencyRemoteToLocalConverter(currencyResponseRoot).convertAll()
        schedulers = Schedulers.newThread()
    }

    @Test
    fun fetchCurrencyTest() {
        `when`(currencyApi.fetchCurrency()).thenReturn(Observable.just(currencyResponseRoot))
        `when`(currencyDao.insertOrUpdateAllCurrency(currencies)).thenReturn(Completable.complete())
        var fetchSubscriber = currencyRepository.fetchCurrency().subscribeOn(schedulers).test()
        fetchSubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        fetchSubscriber.assertNoErrors().assertComplete()
        fetchSubscriber.dispose()
    }

    @Test
    fun getAllCurrencyTest() {
        `when`(currencyApi.fetchCurrency()).thenReturn(Observable.just(currencyResponseRoot))
        `when`(currencyDao.getAllCurrency()).thenReturn(Observable.just(currencies))
        `when`(currencyDao.insertOrUpdateAllCurrency(currencies)).thenReturn(Completable.complete())
        `when`(currencyDao.getCount()).thenReturn(Observable.just(emptyList))
        var getAllSubscriber = currencyRepository.getAllCurrency().subscribeOn(schedulers).test()
        getAllSubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        getAllSubscriber.assertNoErrors().assertValue(currencies)
        getAllSubscriber.dispose()
    }
}