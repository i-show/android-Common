package com.ishow.common.utils.image.compress.wrapper

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */

internal class ImageFileWrapper(val file: File) : ImageWrapper() {

    override fun openStream(): InputStream? {
        return FileInputStream(file)
    }
}