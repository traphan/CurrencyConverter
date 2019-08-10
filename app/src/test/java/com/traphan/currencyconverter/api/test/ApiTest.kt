package com.traphan.currencyconverter.api.test

import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.api.Util
import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import com.traphan.currencyconverter.api.testhelper.CurrencyApiTestHelper
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiTest {

    private var mockWebServer: MockWebServer? = null
    private var mockResponse: MockResponse? = null
    private var currencyApi: CurrencyApi? = null
    private var schedulers: Scheduler? = null
    private var currencyResponseRoot: CurrencyResponseRoot? = null

    @Before
    fun setUp() {
        schedulers = Schedulers.newThread()
        mockWebServer = MockWebServer()

        try {
            mockWebServer!!.start()
            val util = Util()
            val currencyApiTestHelper = CurrencyApiTestHelper(
                util,
                "C:/Users/traph/AndroidStudioProjects/CurrencyConverter/app/src/test/java/com/traphan/currencyconverter/api/resources/currency"
            )
            mockResponse = currencyApiTestHelper.getMockResponse()
            currencyResponseRoot = currencyApiTestHelper.getCurrencyResponse()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val retrofit =
            Retrofit.Builder().baseUrl(mockWebServer!!.url("/")).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        currencyApi = retrofit.create(CurrencyApi::class.java!!)
    }

    @After
    fun out() {
        try {
            mockWebServer!!.shutdown()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun api_Test() {
        mockWebServer!!.enqueue(mockResponse!!)
        val testSubscriber = currencyApi!!.fetchCurrency().subscribeOn(schedulers).test()
        testSubscriber.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS)
        testSubscriber.assertNoErrors()
        Assert.assertTrue(currencyResponseRoot!!.equals(testSubscriber.values()[0]))
    }
}