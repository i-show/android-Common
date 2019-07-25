package com.ishow.noah.modules.account.common

import android.util.Log
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.LoginParams
import com.ishow.noah.manager.RetrofitManager
import com.ishow.noah.modules.base.AppBaseModel
import retrofit2.Response

class AccountModel : AppBaseModel() {
    private val httpService = RetrofitManager.instance.appService

    fun loginByToken(token: String) {
        httpService.loginByToken().execute()
    }

    fun login(phone: String, password: String): AppHttpResponse<UserContainer> {
        val params = LoginParams()
        params.account = phone
        params.password = password
        return request { httpService.login(params).execute() }
    }

}