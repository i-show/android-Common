package com.ishow.common.utils.download

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import okio.Okio
import java.io.FileOutputStream
import java.io.RandomAccessFile


class DownloadJob(val client: OkHttpClient, val info: DownloadInfo, val callback: OnCallBack) : Runnable {
    private var _isFinished: Boolean = false
    internal val isFinished
        get() = _isFinished

    override fun run() {
        val request = Request.Builder()
            .url(info.url)
            .addHeader("Range", "bytes=${info.start}-${info.end}")
            .addHeader("Connection", "Keep-Alive")
            .get()
            .build()

        val response = try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            return
        }
        val body = response.body() ?: return

        val accessFile = RandomAccessFile(info.saveFile, "rwd")
        accessFile.seek(info.start)

        val source = Okio.source(body.byteStream())
        val sink = Okio.sink(FileOutputStream(accessFile.fd))
        val buf = Buffer()

        try {
            do {
                val len = source.read(buf, 1024 * 1024)
                if (len < 0) break
                sink.write(buf, len)
                callback.onLengthChanged(len.toInt())
            } while (true)
        } catch (e: Exception) {
            Log.i("yhy", "DownloadJob e = $e")
        } finally {
            sink.flush()
            sink.close()
            source.close()
        }



        _isFinished = true
        callback.onFinished(info)
    }

    interface OnCallBack {
        fun onLengthChanged(length: Int)

        fun onFinished(info: DownloadInfo)
    }
}