package com.ishow.common.extensions

import android.webkit.MimeTypeMap
import java.io.File

/**
 * 获取File的MimeType
 */
inline val File.mimeType: String?
    get() {
        val extension = MimeTypeMap.getFileExtensionFromUrl(absolutePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
