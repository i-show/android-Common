package com.ishow.noah.data.retrofit

import com.ishow.noah.BuildConfig
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.LoginParams
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AppRestService {
    companion object {
        val BASE_URL: String
            get() =
                when (BuildConfig.VERSION_TYPE) {
                    BuildConfig.VERSION_DEV,
                    BuildConfig.VERSION_SIT,
                    BuildConfig.VERSION_UAT,
                    BuildConfig.VERSION_PROD -> "https://api.yuhaiyang.net/"
                    else -> "https://api.yuhaiyang.net/"
                }
    }


    @POST("common/account/login")
    fun login(@Body params: LoginParams): Call<AppHttpResponse<UserContainer>>


    @POST("common/account/loginByToken")
    fun loginByToken(): Call<UserContainer>
}