package com.ishow.noah.utils.http.retrofit.adapter

import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.http.AppListResponse
import com.ishow.noah.entries.http.AppPageResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AppCallAdapterFactory : CallAdapter.Factory() {


    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawType: Class<*> = getRawType(returnType)
        if (rawType != AppHttpResponse::class.java &&
            rawType != AppListResponse::class.java &&
            rawType != AppPageResponse::class.java
        ) return null
        return AppCallAdapter<Any>(returnType, rawType)
    }

    companion object {
        fun create(): AppCallAdapterFactory = AppCallAdapterFactory()
    }
}