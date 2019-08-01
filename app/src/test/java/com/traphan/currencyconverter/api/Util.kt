package com.traphan.currencyconverter.api

import java.io.File
import java.io.IOException
import java.nio.file.Files

class Util {

    @Throws(IOException::class)
    fun getJson(patch: String): String {
        val file = File(patch)
        return String(Files.readAllBytes(file.toPath()))
    }
}