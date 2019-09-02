package com.traphan.currencyconverter.СurrencyСalculation

import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.ui.CurrencyViewEntity

private fun getCalculateCurrency(currencyEntity: CurrencyEntity): CurrencyViewEntity {
    return CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.value / currencyEntity.nominal, currencyEntity.nominal, currencyEntity.value)
}

fun getCalculateAllCurrency(currencyEntities: List<CurrencyEntity>): List<CurrencyViewEntity> {
    var currencyViewEntitie: List<CurrencyViewEntity> = mutableListOf()
    currencyEntities.forEach{currencyViewEntitie = currencyViewEntitie.plus(getCalculateCurrency(it))}
    return currencyViewEntitie
}