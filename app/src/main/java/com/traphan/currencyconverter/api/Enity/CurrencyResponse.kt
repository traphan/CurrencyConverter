package com.traphan.currencyconverter.api.Enity

data class CurrencyResponse(var ID : String,
                            var NumCode : String,
                            var CharCode : String,
                            var Nominal : Int,
                            var Name : String,
                            var Value : Float,
                            var Previous : Float)
