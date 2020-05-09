package com.ishow.noah.modules.sample.detail.utils.okhttp.cache

import com.ishow.common.utils.http.HttpUtils
import okhttp3.Interceptor

class TestIn {
    val NetCacheInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        val onlineCacheTime = 30 //在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$onlineCacheTime")
            .removeHeader("Pragma")
            .build()
    }

    /**
     * 没有网时候的缓存
     */
    val OfflineCacheInterceptor = Interceptor { chain ->
        var request = chain.request()
        if (!HttpUtils.isConnected) {
            val offlineCacheTime = 60 //离线的时候的缓存的过期时间
            request = request.newBuilder() //                        .cacheControl(new CacheControl
                //                                .Builder()
                //                                .maxStale(60,TimeUnit.SECONDS)
                //                                .onlyIfCached()
                //                                .build()
                //                        ) 两种方式结果是一样的，写法不同
                .header("Cache-Control", "public, only-if-cached, max-stale=$offlineCacheTime")
                .build()
        }
        chain.proceed(request)
    }
}