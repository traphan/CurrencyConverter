package com.traphan.currencyconverter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.traphan.currencyconverter.database.entity.ImageEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAllImage(images: List<ImageEntity>): Completable

    @Query("DELETE FROM images")
    fun deleteAllImage(): Completable

    @Query("SELECT * FROM images")
    fun loadAllImages(): Observable<List<ImageEntity>>

    @Query("SELECT * FROM images WHERE id_remote = :id")
    fun loadImage(id: String): Observable<ImageEntity>

    @Query("SELECT COUNT(1) FROM images")
    fun getCount() : Observable<List<Int>>
}