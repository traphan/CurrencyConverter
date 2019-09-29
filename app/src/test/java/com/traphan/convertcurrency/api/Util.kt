package com.traphan.convertcurrency.api

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.*

class Util {

    @Throws(IOException::class)
    fun getJson(patch: String): String {
        val file = File(patch)
        return String(Files.readAllBytes(file.toPath()))
    }
    fun getRandomString(strLength: Int): String {
        val leftLimit = 97 // letter 'a'
        val rightLimit = 122 // letter 'z'
        val random = Random()
        val buffer = StringBuilder(strLength)
        for (i in 0 until strLength) {
            val randomLimitedInt = leftLimit + (random.nextFloat() * (rightLimit - leftLimit + 1)).toInt()
            buffer.append(randomLimitedInt.toChar())
        }
        return buffer.toString()
    }

    fun getRandomNumberInRange(min: Int, max: Int): Int {
        if (min >= max) {
            throw IllegalArgumentException("max must be greater than min")
        }
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    fun getRandomFloat(min: Float, max: Float): Float {
        val rand = Random()
        return rand.nextFloat() * (max - min) + min
    }
}