package com.traphan.currencyconverter.repository

import com.traphan.currencyconverter.api.Util
import com.traphan.currencyconverter.database.entity.UserCurrency
import java.util.*


class UserCurrencyDaoTestHelper {

    private fun getUserCurrency(): UserCurrency {
        return UserCurrency(
            Util().getRandomNumberInRange(0,1000),
            Util().getRandomString(25),
            false
        )
    }

    internal fun getUserCurrencies(size: Int): List<UserCurrency> {
        val currencyEntities = LinkedList<UserCurrency>()
        var i = 0
        while (i < size) {
            currencyEntities.add(getUserCurrency())
            i++
        }
        return currencyEntities
    }
}