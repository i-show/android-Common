package com.ishow.noah.modules.init.splash.task

import android.content.Context
import android.text.TextUtils
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AccountModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class UserTask(val context: Context) : ITask {
    override fun startAsync() = GlobalScope.async {
        val accessToken = UserManager.instance.getAccessToken(context)
        if (TextUtils.isEmpty(accessToken)) {
            status = Status.None
            return@async
        }

        val accountModel = AccountModel()
        accountModel.loginByToken(accessToken, object : AccountModel.OnLoginCallBack {

            override fun onSuccess(container: UserContainer) {
                status = Status.LoginSuccess
            }

            override fun onFailed(error: HttpError) {
                status = Status.LoginFailed
            }
        })
    }


    private fun login() = GlobalScope.async {

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