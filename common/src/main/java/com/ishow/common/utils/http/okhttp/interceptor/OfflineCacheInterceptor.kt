package com.ishow.common.utils.http.okhttp.interceptor

import com.ishow.common.utils.http.HttpUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OfflineCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val result = request.header(KEY_ENABLE)

        if (!HttpUtils.isConnected && result == "1") {
            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .onlyIfCached()
                .build()

            request = request.newBuilder()
                .cacheControl(cacheControl)
                .build()
        }

        return chain.proceed(request)
    }

    companion object {
        const val KEY_ENABLE = "offline_cache_enable"
    }
}