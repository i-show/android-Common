package com.ishow.common.utils.download.db

import androidx.room.Entity

@Entity(tableName = "file_download", primaryKeys = ["id", "url"])
class DownloadData {
    var id: Int = 0
    var url: String = ""
    var downloadLength: Long = 0L

    var start: Long = 0L
    var end: Long = 0L
    var file: String = ""
}