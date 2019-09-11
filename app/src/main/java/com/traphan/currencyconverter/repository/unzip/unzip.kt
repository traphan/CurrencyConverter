package com.traphan.currencyconverter.repository.unzip

import android.content.Context
import android.os.Environment
import com.traphan.currencyconverter.database.entity.ImageEntity
import io.reactivex.Single
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun unpackZip(inputStream: InputStream, context: Context): Single<List<ImageEntity>> {
    val POSTFIX_ICON = "ICON"
    val positionSubString = 3
    var imageEntities: MutableList<ImageEntity> = mutableListOf()
    val `is`: InputStream
    val zis: ZipInputStream
    try {
        var filename: String
        `is` = inputStream
        zis = ZipInputStream(BufferedInputStream(`is`))
        var ze: ZipEntry?
        val buffer = ByteArray(1024)
        ze = zis.nextEntry
        while (ze != null) {
            filename = ze.name
            val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val fout = FileOutputStream(path.toString() + filename)
            var count: Int = zis.read(buffer)
            while (count != -1) {
                fout.write(buffer, 0, count)
                count = zis.read(buffer)
            }
            fout.close()
            zis.closeEntry()
            val id = filename.substring(0, positionSubString)
            if (filename.substring(positionSubString + 1, filename.length - 4) == POSTFIX_ICON) {
                if(imageEntities.contains(ImageEntity(id, null, null))) {
                    val position = imageEntities.indexOf(ImageEntity(id, null, null))
                    imageEntities[position] = ImageEntity(id, path.toString() + filename, imageEntities[position].images)
                } else {
                    imageEntities.add(ImageEntity(id,path.toString() + filename, null))
                }
            } else {
                if(imageEntities.contains(ImageEntity(id, null, null))) {
                    val position = imageEntities.indexOf(ImageEntity(id, null, null))
                    imageEntities[position] = ImageEntity(id, imageEntities[position].icon, path.toString() + filename)
                } else {
                    imageEntities.add(ImageEntity(id,null, path.toString() + filename))
                }
            }
            ze = zis.nextEntry
        }
        zis.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return Single.just(imageEntities)
    }
    return Single.just(imageEntities)
}
