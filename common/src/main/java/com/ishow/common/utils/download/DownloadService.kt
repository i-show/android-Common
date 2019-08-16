package com.ishow.common.utils.download

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url


interface DownloadService {

    @GET
    @Streaming
    fun download(@Url url: String): Call<ResponseBody>

    @GET
    @Streaming
    fun download(@Header("RANGE") start: String, @Url url: String): Call<ResponseBody>
}