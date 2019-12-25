package com.ishow.common.utils.image.compress.wrapper

import java.io.IOException
import java.io.InputStream

/**
 * Created by yuhaiyang on 2019-12-24.
 * 用来包含
 */
interface IImageWrapper {
    @Throws(IOException::class)
    fun open(): InputStream?

    fun close()
}