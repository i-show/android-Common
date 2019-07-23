package com.ishow.noah.utils.http.retrofit.adapter

import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.utils.http.retrofit.call.BaseCall
import retrofit2.CallAdapter

abstract class AppCallAdapter <R> :CallAdapter<AppHttpResponse<R>, BaseCall<R>>


