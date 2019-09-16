package com.ishow.noah.modules.base.mvvm.model

import android.util.Log
import com.ishow.noah.entries.http.AppHttpResponse
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class AppBaseModel {

    /**
     * 请求网络信息

    protected fun <T> request(block: () -> AppHttpResponse<T>): AppHttpResponse<T> {
        try {
            val response: Response<AppHttpResponse<T>> = block()
            if (response.isSuccessful) {
                return response.body() ?: return AppHttpResponse.empty()
            } else {
                val error = AppHttpResponse<T>()
                error.code = response.code()
                error.message = response.message()
                return error
            }
        } catch (e: Exception) {
            Log.i("yhy", "e = $e")
            return parseException(e)
        }
    }

    private fun <T> parseException(e: Exception): AppHttpResponse<T> {
        return when (e) {
            is IOException,
            is ConnectException,
            is UnknownHostException,
            is SocketTimeoutException -> AppHttpResponse.exception("服务器连接超时，请检查网络后再试")
            else -> AppHttpResponse.exception(e.message)
        }
    }
     */
}