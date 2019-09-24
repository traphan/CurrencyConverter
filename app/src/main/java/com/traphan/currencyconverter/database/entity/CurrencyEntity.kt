package com.traphan.currencyconverter.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "currency")
data class CurrencyEntity (

    @PrimaryKey
    @ColumnInfo(name = "id_remote")
    val idRemote : String,

    @ColumnInfo(name = "num_code")
    val numCode : String,

    @ColumnInfo(name = "char_code")
    val charCode : String,

    @ColumnInfo(name = "nominal")
    val nominal : Int,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name= "value")
    val value : Float,

    @ColumnInfo(name = "previous")
    val previous : Float
)