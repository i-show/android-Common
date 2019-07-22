package com.ishow.noah.modules.account.common

import android.content.Context
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.entries.UserContainer

class AccountModel {

    fun loginByToken(token: String, callBack: OnLoginCallBack? = null) {

    }


    interface OnLoginCallBack {

        fun onSuccess(container: UserContainer)

        fun onFailed(error: HttpError)
    }
}