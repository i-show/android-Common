package com.ishow.common.utils.download

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.io.RandomAccessFile

class DownloadJob(val client: OkHttpClient, val info: DownloadInfo, val callback: OnCallBack) : Runnable {
    private var _status: Status = Status.Running
    internal val status
        get() = _status

    private var _downloadLength: Long = 0L
    private val downloadLength
        get() = _downloadLength

    internal var intercept: Boolean = false
        set(value) {
            field = value
            _status = Status.Intercept
        }

    init {
        _downloadLength = info.downloadLength
    }

    override fun run() {
        _status = Status.Running
        callback.onStatusChanged(this, info, null)

        val request = Request.Builder()
            .url(info.url)
            .addHeader("Range", "bytes=${info.start + downloadLength}-${info.end}")
            .get()
            .build()

        var accessFile: RandomAccessFile? = null
        var inputStream: InputStream? = null

        try {
            val response = client.newCall(request).execute()
            val body = response.body() ?: return

            accessFile = RandomAccessFile(info.saveFile, "rwd")
            accessFile.seek(info.start + downloadLength)

            val buffer = ByteArray(8 * 1024)
            inputStream = body.byteStream()

            // 3. 开始保存文件
            do {
                val length = inputStream.read(buffer)
                if (length < 0) {
                    _status = Status.Finished
                    break
                }
                accessFile.write(buffer, 0, length)
                callback.onLengthChanged(info, length.toLong())
            } while (!intercept)

        } catch (e: Exception) {
            _status = Status.Error
            callback.onStatusChanged(this, info, e.toString())
        } finally {
            inputStream?.close()
            accessFile?.close()
            if (status != Status.Error) {
                callback.onStatusChanged(this, info, null)
            }
        }
    }

    interface OnCallBack {

        fun onLengthChanged(info: DownloadInfo, length: Long)

        fun onStatusChanged(job: DownloadJob, info: DownloadInfo, msg: String?)
    }

    enum class Status {
        Running,
        Error,
        Finished,
        Intercept
    }
}