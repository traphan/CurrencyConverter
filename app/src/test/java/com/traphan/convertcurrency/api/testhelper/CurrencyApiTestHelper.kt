package com.traphan.convertcurrency.api.testhelper

import com.google.gson.GsonBuilder
import com.traphan.convertcurrency.api.Util
import traphan.ren95.convertcurrency.api.entity.CurrencyResponseRoot
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import traphan.ren95.convertcurrency.database.entity.CurrencyJoinImage
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
            currencyJoinImage = currencyJoinImage.plus(
                CurrencyJoinImage(
                    it,
                    listOf()
                )
            )
        }
        return currencyJoinImage
    }
}

