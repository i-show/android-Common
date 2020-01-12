package com.ishow.common.utils.image.compress.wrapper

import com.ishow.common.extensions.mimeType
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */

internal class ImageFileWrapper(val file: File) : ImageWrapper() {
    
    init {
        mimeType = file.mimeType
    }

    override fun openStream(): InputStream? {
        return FileInputStream(file)
    }

    override fun getDescription(): String = file.absolutePath
}