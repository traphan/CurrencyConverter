package com.traphan.currencyconverter.сurrencyсalculation

import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.ui.CurrencyViewEntity

private fun getCalculateCurrency(currencyEntity: CurrencyEntity): CurrencyViewEntity {
    return CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.value / currencyEntity.nominal,
        currencyEntity.nominal.toFloat(), currencyEntity.value)
}

fun getCalculateAllCurrency(currencyEntities: List<CurrencyEntity>): Set<CurrencyViewEntity> {
    var currencyViewEntities: Set<CurrencyViewEntity> = setOf<CurrencyViewEntity>()
    currencyEntities.forEach{currencyViewEntities = currencyViewEntities.plus(getCalculateCurrency(it))}
    return currencyViewEntities
}
fun getCalculationCurrency(currencyEntity: CurrencyViewEntity, nominal: Float): CurrencyViewEntity {
    return CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.rate, nominal, currencyEntity.rate * nominal)
}