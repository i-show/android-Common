package com.ishow.noah.data.retrofit

import com.ishow.noah.BuildConfig
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.http.AppPageResponse
import com.ishow.noah.entries.params.request.ForgotPasswordParams
import com.ishow.noah.entries.params.request.LoginParams
import com.ishow.noah.entries.params.request.RegisterParams
import com.ishow.noah.modules.sample.entries.SampleTestPage
import okhttp3.RequestBody
import retrofit2.http.*

interface AppRestService {
    companion object {
        val BASE_URL: String
            get() =
                when (BuildConfig.VERSION_TYPE) {
                    BuildConfig.VERSION_DEV,
                    BuildConfig.VERSION_SIT,
                    BuildConfig.VERSION_UAT,
                    BuildConfig.VERSION_PROD -> "https://api.yuhaiyang.net/common/"
                    else -> "https://api.yuhaiyang.net/common/"
                }
    }

    /**
     * 登录
     */
    @POST("account/login")
    fun login(@Body params: LoginParams): AppHttpResponse<UserContainer>

    /**
     * 通过Token来处理
     */
    @POST("account/loginByToken")
    fun loginByToken(): AppHttpResponse<UserContainer>

    /**
     * 用户注册
     */
    @POST("account/register")
    fun register(@Body params: RegisterParams): AppHttpResponse<UserContainer>

    /**
     * 忘记密码
     */
    @POST("account/forgotPassword")
    fun forgotPassword(@Body params: ForgotPasswordParams): AppHttpResponse<Any>

    @Multipart
    @POST("account/uploadAvatar")
    fun uploadAvatar(@Part("file\"; filename=\"image.jpg") file: RequestBody): AppHttpResponse<String>

    /**
     * TestPage
     */
    @GET("test/page")
    fun testPage(@Query("page") page: Int, @Query("pageSize") pageSize: Int): AppPageResponse<SampleTestPage>
}