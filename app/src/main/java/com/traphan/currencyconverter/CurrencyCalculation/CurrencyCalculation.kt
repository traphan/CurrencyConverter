package com.traphan.currencyconverter.CurrencyCalculation

import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.ui.CurrencyViewEntity
import io.reactivex.Observable

private fun getCalculateCurrency(currencyEntity: CurrencyJoinImage): CurrencyViewEntity {
    var image = if (currencyEntity.imageEntity != null && currencyEntity.imageEntity.isNotEmpty()) {
        currencyEntity.imageEntity[0].images
    } else {
        null
    }
    return CurrencyViewEntity(currencyEntity.currencyEntity.name, currencyEntity.currencyEntity.charCode, currencyEntity.currencyEntity.value / currencyEntity.currencyEntity.nominal,
        currencyEntity.currencyEntity.nominal.toFloat(), currencyEntity.currencyEntity.value, currencyEntity.currencyEntity.nominal.toString().length,
        image)
}

fun getCalculateAllCurrency(currencyEntities: List<CurrencyJoinImage>): Set<CurrencyViewEntity> {
    var currencyViewEntities: Set<CurrencyViewEntity> = setOf()
    currencyEntities.forEach{currencyViewEntities = currencyViewEntities.plus(getCalculateCurrency(it))}
    return currencyViewEntities
}
fun getCalculationCurrency(currencyEntity: CurrencyViewEntity, nominal: Float): Observable<CurrencyViewEntity> {
    return Observable.just(CurrencyViewEntity(currencyEntity.name, currencyEntity.charCode, currencyEntity.rate, nominal, currencyEntity.rate * nominal, currencyEntity.cursorPosition,
        currencyEntity.patchImage))
}