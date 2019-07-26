package com.ishow.noah.utils.http.retrofit.call

import com.ishow.noah.entries.http.AppHttpResponse

class AppCall<T> : BaseCall<T> {
    override fun execute(): AppHttpResponse<T>? {
        return null
    }

}