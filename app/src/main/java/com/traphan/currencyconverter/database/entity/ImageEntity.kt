package com.traphan.currencyconverter.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    @ForeignKey(entity = CurrencyEntity::class, childColumns = ["id_remote"], parentColumns = ["char_code"])
    @ColumnInfo(name = "id_remote")
    val currencyRemoteId: String,

    @ColumnInfo(name = "icon")
    val icon: String?,

    @ColumnInfo(name = "image")
    val images: String?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ImageEntity
        if (currencyRemoteId != other.currencyRemoteId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = currencyRemoteId.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + images.hashCode()
        return result
    }
}
