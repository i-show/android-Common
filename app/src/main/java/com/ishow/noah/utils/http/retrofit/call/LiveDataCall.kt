package com.ishow.noah.utils.http.retrofit.call

import androidx.lifecycle.LiveData
import com.ishow.noah.entries.http.AppHttpResponse

abstract class LiveDataCall<T> : LiveData<AppHttpResponse<T>>(), BaseCall<T> {
}