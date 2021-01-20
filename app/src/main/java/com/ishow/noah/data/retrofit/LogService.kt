package com.ishow.noah.data.retrofit


import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.http.AppPageResponse
import com.ishow.noah.modules.sample.entries.SampleTestPage
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface LogService {

    @GET("a/log.gif")
    suspend fun log(@QueryMap params: HashMap<String, String>): AppHttpResponse<Any>

    @POST("a/log.gif")
    suspend fun postLog(@QueryMap params: HashMap<String, String>): AppHttpResponse<Any>

    /**
     * TestPage
     */
    @GET("a/log.gif")
    suspend fun testPage(): AppHttpResponse<Any>
}