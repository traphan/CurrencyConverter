package traphan.ren95.convertcurrency.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CurrencyJoinImage (

    @Embedded
    val currencyEntity: CurrencyEntity,

    @Relation(entity = ImageEntity::class, parentColumn = "char_code", entityColumn = "id_remote")
    val imageEntity: List<ImageEntity>
)