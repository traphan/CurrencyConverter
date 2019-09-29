package traphan.ren95.convertcurrency.database.datasourceimpl

import traphan.ren95.convertcurrency.database.dao.ImageDao
import traphan.ren95.convertcurrency.database.datasource.ImageLocal
import traphan.ren95.convertcurrency.database.entity.ImageEntity
import io.reactivex.Completable
import io.reactivex.Observable

class ImageLocalImpl(private val imageDao: ImageDao):
    ImageLocal {

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