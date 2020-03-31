package com.ishow.common.utils.download

import java.io.File

class DownloadInfo(val id: Int, val url: String, val saveFile: File) {

    var start: Long = 0
    var end: Long = 0
    var downloadLength: Long = 0
}