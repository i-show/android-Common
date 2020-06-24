package com.ishow.noah.modules.account.common

import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.ForgotPasswordParams
import com.ishow.noah.entries.params.request.LoginParams
import com.ishow.noah.entries.params.request.RegisterParams
import com.ishow.noah.manager.RetrofitManager
import com.ishow.noah.modules.base.mvvm.model.AppBaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class AppModel private constructor() : AppBaseModel() {
    private val httpService = RetrofitManager.instance.appService

    suspend fun loginByToken(): AppHttpResponse<UserContainer> = withContext(Dispatchers.IO) {
        return@withContext httpService.loginByToken()
    }

    /**
     * 登录
     */
    suspend fun login(phone: String, password: String): AppHttpResponse<UserContainer> = withContext(Dispatchers.IO) {
        val params = LoginParams()
        params.account = phone
        params.password = password
        return@withContext httpService.login(params)
    }

    /**
     * 注册
     */
    suspend fun register(params: RegisterParams): AppHttpResponse<UserContainer> = withContext(Dispatchers.IO) {
        return@withContext httpService.register(params)
    }

    /**
     * 忘记密码
     */
    suspend fun forgotPassword(params: ForgotPasswordParams): AppHttpResponse<Any> = withContext(Dispatchers.IO) {
        return@withContext httpService.forgotPassword(params)
    }

    /**
     * 上传头像
     */
    suspend fun uploadAvatar(path: String): AppHttpResponse<String> = withContext(Dispatchers.IO) {
        val body = RequestBody.create(MediaType.parse("image/jpg"), File(path))
        return@withContext httpService.uploadAvatar(body)
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AppModel() }
    }
}