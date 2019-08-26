package com.traphan.currencyconverter.database.datasourceimpl

import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.datasource.ImageLocal
import com.traphan.currencyconverter.database.entity.ImageEntity
import io.reactivex.Completable
import io.reactivex.Observable

class ImageLocalImpl(private val imageDao: ImageDao): ImageLocal {

    override fun insertOrUpdateAllImage(imageEntities: List<ImageEntity>): Completable {
        return imageDao.insertOrUpdateAllImage(imageEntities)
    }

    override fun deleteAllImage(): Completable {
        return imageDao.deleteAllImage()
    }

    override fun loadAllImages(): Observable<List<ImageEntity>> {
        return imageDao.loadAllImages()
    }

    override fun loadImage(id: String): Observable<ImageEntity> {
        return imageDao.loadImage(id)
    }
}