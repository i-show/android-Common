package com.ishow.noah.utils.http.retrofit.adapter

import com.ishow.noah.entries.http.AppHttpResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Suppress("UNCHECKED_CAST")
class AppCallAdapter<R>(private val type: Type) : CallAdapter<R, R> {

    override fun responseType() = type

    override fun adapt(call: Call<R>): R {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body() ?: AppHttpResponse.empty() as R
            } else {
                val result = AppHttpResponse<R>()
                result.code = response.code()
                result.message = response.message()
                result as R
            }
        } catch (e: Exception) {
            parseException(e) as R
        }

    }


    private fun parseException(e: Exception): AppHttpResponse<*> {
        return when (e) {
            is IOException,
            is ConnectException,
            is UnknownHostException,
            is SocketTimeoutException -> AppHttpResponse.exception("服务器连接超时，请检查网络后再试")
            else -> AppHttpResponse.exception(e.message)
        }
    }
}


