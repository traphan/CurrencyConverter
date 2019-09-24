package com.traphan.currencyconverter

import com.traphan.currencyconverter.database.entity.ImageEntity
import java.util.*

class ImageDaoTestHelper {

    private fun getImageEntity(): ImageEntity {
        return ImageEntity(
            Util.getRandomString(3),
            Util.getRandomString(25),
            Util.getRandomString(25)
        )
    }

    internal fun getImageEntities(size: Int): List<ImageEntity> {
        val currencyEntities = LinkedList<ImageEntity>()
        var i = 0
        while (i < size) {
            currencyEntities.add(getImageEntity())
            i++
        }
        return currencyEntities
    }

    internal fun getImageEntityWithId(id: String): ImageEntity {
        return ImageEntity(
            id,
            Util.getRandomString(25),
            Util.getRandomString(25)
        )
    }

}