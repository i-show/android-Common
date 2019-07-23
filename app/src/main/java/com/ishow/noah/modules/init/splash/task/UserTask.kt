package com.ishow.noah.modules.init.splash.task

import android.content.Context
import android.text.TextUtils
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
        accountModel.loginByToken(accessToken)
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