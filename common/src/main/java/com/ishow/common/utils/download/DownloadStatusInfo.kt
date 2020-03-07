package com.ishow.common.utils.download

import java.io.File

class DownloadStatusInfo(val status: DownloadStatus) {
    var errorCode: Int = 0
    var errorMessage: String = ""
    var file: File? = null

    override fun toString(): String {
        return "DownloadStatusInfo(status=$status, errorCode=$errorCode, errorMessage='$errorMessage', file=$file)"
    }
}

