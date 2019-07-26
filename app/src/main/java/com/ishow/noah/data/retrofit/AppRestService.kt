package com.ishow.noah.data.retrofit

import com.ishow.noah.BuildConfig
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.LoginParams
import com.ishow.noah.entries.params.request.RegisterParams
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


    /**
     * 登录
     */
    @POST("common/account/login")
    fun login(@Body params: LoginParams): Call<AppHttpResponse<UserContainer>>

    /**
     * 通过Token来处理
     */
    @POST("common/account/loginByToken")
    fun loginByToken(): Call<UserContainer>

    /**
     * 用户注册
     */
    @POST("common/account/register")
    fun register(@Body params: RegisterParams): Call<AppHttpResponse<UserContainer>>
}