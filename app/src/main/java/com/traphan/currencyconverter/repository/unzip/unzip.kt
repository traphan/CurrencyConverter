package com.traphan.currencyconverter.repository.unzip

import android.os.Environment
import io.reactivex.Single
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun unpackZip(inputStream: InputStream): Single<Map<String, String>> {
    val `is`: InputStream
    val zis: ZipInputStream
    val patches = mutableMapOf<String, String>()
    try {
        var filename: String
        `is` = inputStream
        zis = ZipInputStream(BufferedInputStream(`is`))
        var ze: ZipEntry?
        val buffer = ByteArray(1024)
        ze = zis.nextEntry
        while (ze != null) {
            filename = ze.name
            if (ze.isDirectory) {
                val fmd = File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + filename).toString() + filename)
                fmd.mkdirs()
                continue
            }
            val fout = FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + filename)
            var count: Int = zis.read(buffer)
            while (count != -1) {
                fout.write(buffer, 0, count)
                count = zis.read(buffer)
            }
            fout.close()
            zis.closeEntry()
            patches[filename.substring(0, filename.length - 4)] = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + filename
            ze = zis.nextEntry
        }
        zis.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return Single.just(patches)
    }
    return Single.just(patches)
}
