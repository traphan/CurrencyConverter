package com.traphan.currencyconverter.repository.unzip

//import android.util.Log
//import java.io.*
//import java.util.zip.*
//
//val BUFFER_SIZE = 8192
//
//    @Throws(IOException::class)
//    fun compress(data: ByteArray): ByteArray {
//        val deflater = Deflater()
//        deflater.setInput(data)
//        val outputStream = ByteArrayOutputStream(data.size)
//        deflater.finish()
//        val buffer = ByteArray(1024)
//        while (!deflater.finished()) {
//            val count = deflater.deflate(buffer)
//            outputStream.write(buffer, 0, count)
//        }
//        outputStream.close()
//        return outputStream.toByteArray()
//    }
//
//    @Throws(IOException::class, DataFormatException::class)
//    fun decompress(data: ByteArray): ByteArray {
//
//        val inflater = Inflater(true)
//        inflater.setInput(data)
//        val outputStream = ByteArrayOutputStream(data.size)
//        val buffer = ByteArray(1024)
//        while (!inflater.finished()) {
//            val count = inflater.inflate(buffer)
//            outputStream.write(buffer, 0, count)
//        }
//        outputStream.close()
//        return outputStream.toByteArray()
//    }
//
//@Throws(IOException::class)
//fun unzip(zipFile: String, location: String) {
//    var location = location
//    var size: Int
//    val buffer = ByteArray(BUFFER_SIZE)
//
//    try {
//        if (!location.endsWith(File.separator)) {
//            location += File.separator
//        }
//        val f = File(location)
//        if (!f.isDirectory) {
//            f.mkdirs()
//        }
//        val zin = ZipInputStream(BufferedInputStream(FileInputStream(zipFile), BUFFER_SIZE))
//        try {
//            var ze: ZipEntry? = zin.nextEntry
//            while (ze  != null) {
//                val path = location + ze!!.name
//                val unzipFile = File(path)
//
//                if (ze.isDirectory) {
//                    if (!unzipFile.isDirectory) {
//                        unzipFile.mkdirs()
//                    }
//                } else {
//                    // check for and create parent directories if they don't exist
//                    val parentDir = unzipFile.parentFile
//                    if (null != parentDir) {
//                        if (!parentDir.isDirectory) {
//                            parentDir.mkdirs()
//                        }
//                    }
//
//                    // unzip the file
//                    val out = FileOutputStream(unzipFile, false)
//                    val fout = BufferedOutputStream(out, BUFFER_SIZE)
//                    try {
//                        while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
//                            fout.write(buffer, 0, size)
//                        }
//
//                        zin.closeEntry()
//                    } finally {
//                        fout.flush()
//                        fout.close()
//                    }
//                }
//            }
//        } finally {
//            zin.close()
//        }
//    } catch (e: Exception) {
//        Log.e("1", "Unzip exception", e)
//    }
//
//}
//
//
