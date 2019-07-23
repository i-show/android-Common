package com.ishow.noah.data.retrofit

import com.ishow.noah.BuildConfig
import com.ishow.noah.entries.UserContainer
import retrofit2.Call
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
    fun login(): Call<UserContainer>


    @POST("common/account/loginByToken")
    fun loginByToken(): Call<UserContainer>
}