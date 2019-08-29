package com.ishow.common.utils.http.retrofit.adapter

import com.ishow.common.entries.http.HttpResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CallAdapterFactory : CallAdapter.Factory() {


    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != HttpResponse::class.java) return null
        return Adapter<Any>(returnType)
    }

    companion object {
        fun create(): CallAdapterFactory {
            return CallAdapterFactory()
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Adapter<R>(private val type: Type) : CallAdapter<R, R> {

        override fun responseType() = type

        override fun adapt(call: Call<R>): R {
            return try {
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body() ?: HttpResponse.empty() as R
                } else {
                    val result = HttpResponse<R>()
                    result.code = response.code()
                    result.message = response.message()
                    result as R
                }
            } catch (e: Exception) {
                parseException(e) as R
            }

        }


        private fun parseException(e: Exception): HttpResponse<*> {
            return when (e) {
                is IOException,
                is ConnectException,
                is UnknownHostException,
                is SocketTimeoutException -> HttpResponse.exception("服务器连接超时，请检查网络后再试")
                else -> HttpResponse.exception(e.message)
            }
        }
    }
}