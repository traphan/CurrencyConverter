package traphan.ren95.convertcurrency.database.datasource

import traphan.ren95.convertcurrency.database.entity.ImageEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface ImageLocal {

    fun insertOrUpdateAllImage(imageEntities: List<ImageEntity>): Completable

    fun deleteAllImage(): Completable

    fun loadAllImages(): Observable<List<ImageEntity>>

    fun loadImage(id: String): Observable<ImageEntity>
}