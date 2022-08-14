package com.nasa.apod.data.utils

import java.io.IOException
import java.io.InputStream

class JsonParserHelper {
    fun inputStreamToString(inputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            ""
        }
    }
}