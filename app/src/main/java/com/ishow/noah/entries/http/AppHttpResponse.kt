package com.ishow.noah.entries.http

class AppHttpResponse<T> {
    var code: Int = 0
    var message: String? = null
    var value: T? = null


    fun isSuccess() = code == Code.Success

    companion object {

        fun <T> empty(code: Int = Code.Failed): AppHttpResponse<T> {
            val response = AppHttpResponse<T>()
            response.code = code
            response.message = "暂无数据"
            return response
        }

        fun <T> exception(message: String?, code: Int = Code.Failed): AppHttpResponse<T> {
            val response = AppHttpResponse<T>()
            response.code = code
            response.message = message
            return response
        }
    }

    object Code {
        /**
         * 返回成功
         */
        const val Success = 0

        /**
         * 返回失败
         */
        const val Failed = 1
    }


}
