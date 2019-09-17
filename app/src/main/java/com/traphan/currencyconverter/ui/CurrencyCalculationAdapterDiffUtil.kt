package com.traphan.currencyconverter.ui

import androidx.recyclerview.widget.DiffUtil

class CurrencyCalculationAdapterDiffUtil(oldCurrencies: List<CurrencyViewEntity>, newCurrencies: List<CurrencyViewEntity>): DiffUtil.Callback() {

    private val oldCurrencies: List<CurrencyViewEntity> = oldCurrencies
    private val newCurrencies: List<CurrencyViewEntity> = newCurrencies


    override fun getOldListSize(): Int {
        return oldCurrencies.size
    }

    override fun getNewListSize(): Int {
        return newCurrencies.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCurrency = oldCurrencies[oldItemPosition]
        val newCurrency = newCurrencies[newItemPosition]
        return oldCurrency == newCurrency
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCurrency = oldCurrencies[oldItemPosition]
        val newCurrency = newCurrencies[newItemPosition]
        return oldCurrency.total == newCurrency.total && oldCurrency.currentNominal == newCurrency.currentNominal && oldCurrency.rate == newCurrency.rate
    }
}