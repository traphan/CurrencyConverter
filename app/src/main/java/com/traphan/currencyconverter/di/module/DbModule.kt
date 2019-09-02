package com.traphan.currencyconverter.di.module

import android.app.Application
import androidx.room.Room
import com.traphan.currencyconverter.database.AppDatabase
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.dao.UserCurrencyDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "databases.db").build()
    }


    @Provides
    @Singleton
    internal fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    @Singleton
    internal fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.imageDao()
    }

    @Provides
    @Singleton
    internal fun provideUserCurrencyDao(appDatabase: AppDatabase): UserCurrencyDao {
        return appDatabase.userCurrencyDao()
    }
}