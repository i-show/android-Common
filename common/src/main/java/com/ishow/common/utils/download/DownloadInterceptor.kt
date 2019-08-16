package com.ishow.common.utils.download

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class DownloadInterceptor(private val listener: DownloadManager.OnProgressListener?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val originResponse = chain.proceed(chain.request())

        originResponse.body()?.let {
            return originResponse.newBuilder()
                    .body(DownloadResponseBody(it, listener))
                    .build()
        }

        return null
    }
}
