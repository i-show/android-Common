package com.ishow.noah.utils.http.retrofit.call

import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.utils.http.AppHttpCallBack


interface BaseCall<T> {

    fun execute(): AppHttpResponse<T>?
}