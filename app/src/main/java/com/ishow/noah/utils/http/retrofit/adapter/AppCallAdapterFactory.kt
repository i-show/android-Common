package com.ishow.noah.utils.http.retrofit.adapter

import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.http.AppListResponse
import com.ishow.noah.entries.http.AppPageResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AppCallAdapterFactory : CallAdapter.Factory() {

    @Suppress("MoveVariableDeclarationIntoWhen")
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        return when (rawType) {
            AppHttpResponse::class.java,
            AppListResponse::class.java,
            AppPageResponse::class.java -> AppCallAdapter<AppHttpResponse<*>>(returnType, rawType)
            else -> null
        }

    }

    companion object {
        fun create(): AppCallAdapterFactory = AppCallAdapterFactory()
    }
}