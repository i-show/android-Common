package com.ishow.common.utils.image.compress.wrapper

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */
abstract class ImageWrapper {
    companion object {
        private const val TAG = "ImageWrapper"

        fun uri(context: Context, uri: Uri): ImageWrapper {
            return ImageUriWrapper(context, uri)
        }

        fun file(file: File): ImageWrapper {
            return ImageFileWrapper(file)
        }
    }

    private var inputStream: InputStream? = null

    fun open(): InputStream? {
        close()
        inputStream = openStream()
        return inputStream
    }

    fun close() {
        try {
            inputStream?.close()
        } catch (ex: IOException) {
            Log.i(TAG, "ex = $ex")
        } finally {
            inputStream = null
        }
    }


    @Throws(IOException::class)
    abstract fun openStream(): InputStream?

    abstract fun getDescription(): String
}