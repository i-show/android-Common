package com.ishow.noah.modules.account.common

import com.ishow.noah.manager.RetrofitManager

class AccountModel {

    fun loginByToken(token: String) {
        val httpService = RetrofitManager.instance.appService
        httpService.loginByToken().execute()
    }
}