package com.traphan.currencyconverter.ui.recyclerdragandrop.entity

data class BaseCurrencyItem(val name: String, val patch: String): BaseCurrencyViewEntity(
    RECYCLER_CARD_ITEM_TYPE.ITEM
)