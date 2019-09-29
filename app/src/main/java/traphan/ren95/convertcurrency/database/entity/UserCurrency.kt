package traphan.ren95.convertcurrency.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "user_currency")
data class UserCurrency(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int ?,


    @ColumnInfo(name = "id_currency")
    @ForeignKey(entity = CurrencyEntity::class, childColumns = ["id_currency"], parentColumns = ["id_remote"])
    val idCurrency: String,

    @ColumnInfo(name = "is_base")
    val isBase: Boolean)