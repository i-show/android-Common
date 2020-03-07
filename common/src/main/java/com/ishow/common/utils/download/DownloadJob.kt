package com.ishow.common.utils.download

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.RandomAccessFile

class DownloadJob(val client: OkHttpClient, val info: DownloadInfo, val callback: OnCallBack) : Runnable {
    private var _isFinished: Boolean = false
    internal val isFinished
        get() = _isFinished

    override fun run() {
        val request = Request.Builder()
            .url(info.url)
            .addHeader("Range", "bytes=${info.start}-${info.end}")
            .get()
            .build()

        val response = client.newCall(request).execute()
        val body = response.body() ?: return

        val accessFile = RandomAccessFile(info.saveFile, "rwd")
        accessFile.seek(info.start)

        val buffer = ByteArray(1024 * 1024)
        val inputStream = body.byteStream()

        // 3. 开始保存文件
        do {
            val length = inputStream.read(buffer)
            if (length < 0) {
                break
            }
            accessFile.write(buffer, 0, length)
            callback.onLengthChanged(length)
        } while (true)

        inputStream.close()
        accessFile.close()
        _isFinished = true
        callback.onFinished(info)
    }

    interface OnCallBack {
        fun onLengthChanged(length: Int)

        fun onFinished(info: DownloadInfo)
    }
}