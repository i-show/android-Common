package com.ishow.noah.utils.http.retrofit.adapter

import com.ishow.noah.entries.http.AppHttpResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AppCallAdapterFactory : CallAdapter.Factory() {


    override fun get(returnType: Type,
                     annotations: Array<Annotation>,
                     retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != AppHttpResponse::class.java) return null
        return AppCallAdapter<Any>(returnType)
    }

    companion object{
        fun create(): AppCallAdapterFactory {
            return AppCallAdapterFactory()
        }
    }
}