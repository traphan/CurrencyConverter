package com.traphan.currencyconverter

import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import java.util.*

class CurrencyJoinImageTestHelper {


    fun getCurrencyJoinImage(): CurrencyJoinImage {
        val currencyEntity = CurrencyTestHelper.getCurrency()
        val imageCurrency = ImageDaoTestHelper().getImageEntityWithId(currencyEntity.idRemote)
        return CurrencyJoinImage(currencyEntity, listOf(imageCurrency))
    }

    fun getCurrenciesJoinImage(size: Int): List<CurrencyJoinImage> {
        val currencyEntities = LinkedList<CurrencyJoinImage>()
        var i = 0
        while (i < size) {
            currencyEntities.add(getCurrencyJoinImage())
            i++
        }
        return currencyEntities
    }
}