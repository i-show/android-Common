package com.ishow.common.utils.http.retrofit.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.io.IOException

/**
 * Retrofit 转换成String的Adapter
 */
class StringCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return if (returnType != String::class.java) {
            null
        } else {
            StringCallAdapter()
        }
    }

    internal class StringCallAdapter : CallAdapter<String, String> {
        override fun adapt(call: Call<String>): String? {
            try {
                return call.execute().body()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }


        override fun responseType(): Type {
            return String::class.java
        }
    }
}