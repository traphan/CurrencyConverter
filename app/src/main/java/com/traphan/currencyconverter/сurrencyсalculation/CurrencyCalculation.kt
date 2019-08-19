package com.traphan.currencyconverter.сurrencyсalculation

import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.ui.CurrencyViewEntity

private fun getCalculateCurrency(currencyEntity: CurrencyEntity): CurrencyViewEntity {
    return CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.value / currencyEntity.nominal, currencyEntity.nominal, currencyEntity.value)
}

fun getCalculateAllCurrency(currencyEntities: List<CurrencyEntity>): List<CurrencyViewEntity> {
    var currencyViewEntities: List<CurrencyViewEntity> = mutableListOf()
    currencyEntities.forEach{currencyViewEntities = currencyViewEntities.plus(getCalculateCurrency(it))}
    return currencyViewEntities
}