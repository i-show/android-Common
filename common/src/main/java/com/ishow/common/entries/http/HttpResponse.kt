package com.ishow.common.entries.http

class HttpResponse<T> {
    var code: Int = 0
    var message: String? = null
    var data: T? = null


    fun isSuccess() = code == Code.Success

    override fun toString(): String {
        return "HttpResponse(code=$code, message=$message, data=$data)"
    }

    companion object {

        fun empty(code: Int = Code.Failed): HttpResponse<*> {
            val response = HttpResponse<Any>()
            response.code = code
            response.message = "暂无数据"
            return response
        }

        fun exception(message: String?, code: Int = Code.Failed): HttpResponse<*> {
            val response = HttpResponse<Any>()
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
