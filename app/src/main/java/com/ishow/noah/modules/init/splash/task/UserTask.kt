package com.ishow.noah.modules.init.splash.task

import android.text.TextUtils
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class UserTask : ITask {
    override fun startAsync() = GlobalScope.async {
        val accessToken = UserManager.instance.getAccessToken()
        if (TextUtils.isEmpty(accessToken)) {
            status = Status.None
            return@async
        }
        AppHttpInterceptor.token = accessToken
        val result = AccountModel().loginByToken()

        status = if (result.isSuccess()) {
            UserManager.instance.setUserContainer(result.data)
            Status.LoginSuccess
        } else {
            Status.LoginFailed
        }
    }


    companion object {
        internal var status: Status? = Status.None
    }

    enum class Status {
        /**
         * 无Token信息可直接进行下一步
         */
        None,
        /**
         * 登录成功
         */
        LoginSuccess,
        /**
         * 登录失败
         */
        LoginFailed,
    }
}