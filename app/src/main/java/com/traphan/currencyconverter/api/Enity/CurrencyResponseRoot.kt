package com.traphan.currencyconverter.api.Enity

data class CurrencyResponseRoot(val Date : String,
                                val PreviousDate : String,
                                val PreviousURL : String,
                                val Timestamp : String,
                                val Valute : List<CurrencyResponseWrapper>)
