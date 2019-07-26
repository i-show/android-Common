package com.ishow.noah.modules.account.common

import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.ForgotPasswordParams
import com.ishow.noah.entries.params.request.LoginParams
import com.ishow.noah.entries.params.request.RegisterParams
import com.ishow.noah.manager.RetrofitManager
import com.ishow.noah.modules.base.AppBaseModel

class AccountModel : AppBaseModel() {
    private val httpService = RetrofitManager.instance.appService

    fun loginByToken(token: String) {
        httpService.loginByToken().execute()
    }

    /**
     * 登录
     */
    fun login(phone: String, password: String): AppHttpResponse<UserContainer> {
        val params = LoginParams()
        params.account = phone
        params.password = password
        return request { httpService.login(params) }
    }

    /**
     * 注册
     */
    fun register(params: RegisterParams): AppHttpResponse<UserContainer> {
        return request { httpService.register(params) }
    }

    /**
     * 忘记密码
     */
    fun forgotPassword(params: ForgotPasswordParams): AppHttpResponse<Any> {
        return request { httpService.forgotPassword(params) }
    }


}