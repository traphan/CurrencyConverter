package traphan.ren95.convertcurrency.database

import androidx.room.Database
import androidx.room.RoomDatabase
import traphan.ren95.convertcurrency.database.dao.CurrencyDao
import traphan.ren95.convertcurrency.database.dao.ImageDao
import traphan.ren95.convertcurrency.database.dao.UserCurrencyDao
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import traphan.ren95.convertcurrency.database.entity.ImageEntity
import traphan.ren95.convertcurrency.database.entity.UserCurrency

@Database(entities = [CurrencyEntity::class, ImageEntity::class, UserCurrency::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao() : CurrencyDao

    abstract fun imageDao(): ImageDao

    abstract fun userCurrencyDao(): UserCurrencyDao
}