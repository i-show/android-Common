package com.ishow.common.utils.http.okhttp.interceptor

import com.ishow.common.utils.StringUtils
import okhttp3.Interceptor
import okhttp3.Response

class NetCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val cacheTimeStr = request.header(CACHE_TIME)
        val cacheTime = StringUtils.format2Int(cacheTimeStr ?: "0")
        if(cacheTime == 0){
            return chain.proceed(request)
        }

        val response = chain.proceed(request)
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$cacheTime")
            .removeHeader("Pragma")
            .build()
    }

    companion object {
        const val CACHE_TIME = "net_cache_time"
    }
}