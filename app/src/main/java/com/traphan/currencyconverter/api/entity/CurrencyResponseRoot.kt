package com.traphan.currencyconverter.api.entity

data class CurrencyResponseRoot(val Date : String,
                                val PreviousDate : String,
                                val PreviousURL : String,
                                val Timestamp : String,
                                val Valute : CurrencyResponseWrapper)

