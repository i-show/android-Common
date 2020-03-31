package com.ishow.common.utils.download

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import okio.Okio
import okio.Sink
import okio.Source
import java.io.FileOutputStream
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

        var source: Source? = null
        var sink: Sink? = null

        try {
            val response = client.newCall(request).execute()
            val body = response.body() ?: return

            val accessFile = RandomAccessFile(info.saveFile, "rwd")
            accessFile.seek(info.start + downloadLength)

            source = Okio.source(body.byteStream())
            sink = Okio.sink(FileOutputStream(accessFile.fd))
            val buf = Buffer()

            do {
                val len = source.read(buf, 1024 * 1024)
                if (len < 0) {
                    _status = Status.Finished
                    break
                }
                _downloadLength += len
                sink.write(buf, len)
                info.downloadLength += len
                callback.onLengthChanged(info, len)
            } while (!intercept)

        } catch (e: Exception) {
            _status = Status.Error
            callback.onStatusChanged(this, info, e.toString())
        } finally {
            sink?.flush()
            sink?.close()
            source?.close()

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