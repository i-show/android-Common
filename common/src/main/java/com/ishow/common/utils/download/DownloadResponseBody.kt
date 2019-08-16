package com.ishow.common.utils.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class DownloadResponseBody(private val responseBody: ResponseBody, private val listener: DownloadManager.OnProgressListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var progress = 0L
            val total = contentLength()

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                progress += if (bytesRead != -1L) bytesRead else 0
                listener?.progress(progress, total)
                return bytesRead
            }
        }
    }
}