package com.traphan.currencyconverter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.entity.CurrencyEntity
import com.traphan.currencyconverter.database.entity.ImageEntity

@Database(entities = [CurrencyEntity::class, ImageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao() : CurrencyDao

    abstract fun imageDao(): ImageDao
}