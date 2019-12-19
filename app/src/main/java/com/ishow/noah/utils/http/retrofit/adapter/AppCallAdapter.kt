package com.ishow.noah.utils.http.retrofit.adapter

import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.http.AppListResponse
import com.ishow.noah.entries.http.AppPageResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Suppress("UNCHECKED_CAST")
class AppCallAdapter<R>(private val type: Type, private val rawType: Class<*>) : CallAdapter<R, R> {

    override fun responseType() = type

    override fun adapt(call: Call<R>): R {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body() ?: emptyResponse()
            } else {
                errorResponse(response)
            }
        } catch (e: Exception) {
            parseException(e)
        }
    }


    private fun parseException(e: Exception): R {
        return when (e) {
            is IOException,
            is ConnectException,
            is UnknownHostException,
            is SocketTimeoutException -> exceptionResponse("服务器连接超时，请检查网络后再试")
            else -> exceptionResponse(e.message)
        }
    }

    private fun errorResponse(response: Response<R>): R {
        return when (rawType) {
            AppHttpResponse::class.java -> error(AppHttpResponse<Any>(), response) as R
            AppListResponse::class.java -> error(AppListResponse<Any>(), response) as R
            AppPageResponse::class.java -> error(AppPageResponse<Any>(), response) as R
            else -> throw IllegalStateException("rawType Error")
        }
    }

    private fun emptyResponse(): R {
        return when (rawType) {
            AppHttpResponse::class.java -> empty(AppHttpResponse<Any>()) as R
            AppListResponse::class.java -> empty(AppListResponse<Any>()) as R
            AppPageResponse::class.java -> empty(AppPageResponse<Any>()) as R
            else -> throw IllegalStateException("rawType Error")
        }
    }

    private fun exceptionResponse(message: String?): R {
        return when (rawType) {
            AppHttpResponse::class.java -> exception(AppHttpResponse<Any>(), message) as R
            AppListResponse::class.java -> exception(AppListResponse<Any>(), message) as R
            AppPageResponse::class.java -> exception(AppPageResponse<Any>(), message) as R
            else -> throw IllegalStateException("rawType Error")
        }
    }


    companion object {

        fun <T : AppHttpResponse<*>> empty(response: T, code: Int = AppHttpResponse.Code.Failed): T {
            response.code = code
            response.message = "暂无数据"
            return response
        }

        fun <T : AppHttpResponse<*>> error(response: T, retrofitResponse: Response<*>): T {
            response.code = retrofitResponse.code()
            response.message = retrofitResponse.message()
            return response
        }

        fun <T : AppHttpResponse<*>> exception(
            response: T,
            message: String?,
            code: Int = AppHttpResponse.Code.Failed
        ): AppHttpResponse<*> {
            response.code = code
            response.message = message
            return response
        }
    }
}


