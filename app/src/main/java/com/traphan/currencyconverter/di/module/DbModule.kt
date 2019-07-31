package com.traphan.currencyconverter.di.module

import android.app.Application
import androidx.room.Room
import com.traphan.currencyconverter.database.AppDatabase
import com.traphan.currencyconverter.database.dao.CurrencyDao
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
    internal fun provideMovieDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }
}