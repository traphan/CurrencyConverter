package com.traphan.currencyconverter.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    @ForeignKey(entity = CurrencyEntity::class, childColumns = ["id_remote"], parentColumns = ["id_remote"])
    @ColumnInfo(name = "id_remote")
    val currencyRemoteId: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "image")
    val images: ByteArray) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        if (currencyRemoteId != other.currencyRemoteId) return false
        if (!images.contentEquals(other.images)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currencyRemoteId.hashCode()
        result = 31 * result + images.contentHashCode()
        return result
    }
}