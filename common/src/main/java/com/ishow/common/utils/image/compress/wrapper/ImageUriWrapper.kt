package com.ishow.common.utils.image.compress.wrapper

import android.content.Context
import android.net.Uri
import java.io.InputStream

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */

internal class ImageUriWrapper(val context: Context, val uri: Uri) : ImageWrapper() {

    override fun openStream(): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    override fun getDescription(): String = uri.toString()
}