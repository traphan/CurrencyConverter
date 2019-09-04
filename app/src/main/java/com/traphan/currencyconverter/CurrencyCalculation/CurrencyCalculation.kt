package com.traphan.currencyconverter.CurrencyCalculation

import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.ui.CurrencyViewEntity

private fun getCalculateCurrency(currencyEntity: CurrencyJoinImage): CurrencyViewEntity {
    var image = if (currencyEntity.imageEntity != null && currencyEntity.imageEntity.isNotEmpty()) {
        currencyEntity.imageEntity[0].images
    } else {
        null
    }
    val rate = currencyEntity.currencyEntity.value / currencyEntity.currencyEntity.nominal
    return CurrencyViewEntity(currencyEntity.currencyEntity.name, currencyEntity.currencyEntity.charCode, rate, 1f, rate, 1, 1, image)
}

private fun getCalculateAllCurrency(currencyEntities: List<CurrencyJoinImage>): Set<CurrencyViewEntity> {
    var currencyViewEntities: Set<CurrencyViewEntity> = setOf()
    currencyEntities.forEach{currencyViewEntities = currencyViewEntities.plus(getCalculateCurrency(it))}
    return currencyViewEntities
}

fun getCalculationCurrency(currencyEntity: CurrencyViewEntity, nominal: Float, total:Float): CurrencyViewEntity {
    return if (total.equals(currencyEntity.total)) {
        CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.rate, nominal, currencyEntity.rate * nominal, currencyEntity.cursorPositionNominal,
            currencyEntity.cursorPositionTotal, currencyEntity.patchImage)
    } else {
        CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.rate, total / currencyEntity.rate,
            total, currencyEntity.cursorPositionNominal, currencyEntity.cursorPositionTotal, currencyEntity.patchImage)
    }
}

private fun getCalculationCurrencyWithBase(currencyEntity: CurrencyJoinImage, baseCurrency: CurrencyJoinImage): CurrencyViewEntity {
    var image = if (currencyEntity.imageEntity != null && currencyEntity.imageEntity.isNotEmpty()) {
        currencyEntity.imageEntity[0].images
    } else {
        null
    }
    return if (currencyEntity.currencyEntity.idRemote == "BASE_CURRENCY_RUB") {
        val rate =  (baseCurrency.currencyEntity.value / baseCurrency.currencyEntity.nominal) / (currencyEntity.currencyEntity.value / currencyEntity.currencyEntity.nominal)
        CurrencyViewEntity(currencyEntity.currencyEntity.name, currencyEntity.currencyEntity.charCode, rate, 1f, rate , 1,1, image)
    } else {
        val rate =  (baseCurrency.currencyEntity.value / baseCurrency.currencyEntity.nominal) / (currencyEntity.currencyEntity.value / currencyEntity.currencyEntity.nominal)
        CurrencyViewEntity(currencyEntity.currencyEntity.name, currencyEntity.currencyEntity.charCode, rate, 1f, rate, 1, 1, image)
    }
}

private fun getCalculationAllCurrencyWithBase(currencyEntity: List<CurrencyJoinImage>, baseCurrency: CurrencyJoinImage): Set<CurrencyViewEntity> {
    var currencyViewEntities: Set<CurrencyViewEntity> = setOf()
    currencyEntity.forEach{currencyViewEntities = currencyViewEntities.plus(getCalculationCurrencyWithBase(it, baseCurrency))}
    return currencyViewEntities
}

fun getCalculationAllCurrency(currencyEntities: List<CurrencyJoinImage>, baseCurrency: CurrencyJoinImage): Set<CurrencyViewEntity> {
    return if (baseCurrency.currencyEntity.idRemote == "BASE_CURRENCY_RUB") {
        getCalculateAllCurrency(currencyEntities)
    } else {
        getCalculationAllCurrencyWithBase(currencyEntities, baseCurrency)
    }
}

