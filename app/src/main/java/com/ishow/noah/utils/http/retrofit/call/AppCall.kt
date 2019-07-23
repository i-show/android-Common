package com.ishow.noah.utils.http.retrofit.call

import com.ishow.noah.entries.http.AppHttpResponse

class AppCall<T> : LiveDataCall<T>() {
    override fun execute(): AppHttpResponse<T>? {
        return null
    }

}