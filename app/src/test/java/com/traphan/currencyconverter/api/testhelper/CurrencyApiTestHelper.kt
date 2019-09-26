package com.traphan.currencyconverter.api.testhelper

import com.google.gson.GsonBuilder
import com.traphan.currencyconverter.api.Util
import com.traphan.currencyconverter.api.entity.CurrencyResponseRoot
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.ImageEntity
import okhttp3.mockwebserver.MockResponse
import java.io.IOException

internal class CurrencyApiTestHelper(private val util: Util, private val patch: String) {

    @Throws(IOException::class)
    fun getMockResponse(): MockResponse {
        return MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache").setResponseCode(200)
            .setBody(util.getJson(patch))
    }

    @Throws(IOException::class)
    fun getCurrencyResponse(): CurrencyResponseRoot {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.fromJson(util.getJson(patch), CurrencyResponseRoot::class.java)
    }

    @Throws(IOException::class)
    fun getCurrencyJoinImage(curencies: List<CurrencyEntity>): List<CurrencyJoinImage> {
        var currencyJoinImage: List<CurrencyJoinImage> = listOf()
        curencies.forEach{
            currencyJoinImage = currencyJoinImage.plus(CurrencyJoinImage(it, listOf()))
        }
        return currencyJoinImage
    }
}

