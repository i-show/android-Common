/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.account.login

import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.ishow.common.entries.status.Error
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.constant.Configure
import com.ishow.noah.constant.Url
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.UserManager
import com.ishow.noah.utils.http.AppHttpCallBack

/**
 * Created by yuhaiyang on 2018/8/8.
 * 登录的Presenter
 */
internal class LoginPresenter(private val mView: LoginContract.View) : LoginContract.Presenter {

    override fun init() {
        val account = StorageUtils.with(mView.context)
            .key(UserContainer.Key.ACCOUNT)
            .get(StringUtils.EMPTY)

        mView.updateUI(account)
        clear()
    }

    override fun login(account: String, password: String) {
        var errorMessage = UserManager.checkAccount(mView.context, account)
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(Error.dialog(errorMessage))
            return
        }

        errorMessage = UserManager.checkPassword(mView.context, password)
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(Error.dialog(errorMessage))
            return
        }

        saveUserInfo(account)

        mView.showLoading()

        val params = JSONObject()
        params["account"] = account
        params["password"] = password
        params["device"] = Configure.DEVICE
        params["deviceModel"] = DeviceUtils.model
        params["deviceVersion"] = DeviceUtils.version

        Http.post()
            .url(Url.login())
            .params(params.toJSONString())
            .execute(object : AppHttpCallBack<UserContainer>(mView.context) {
                override fun onFailed(error: HttpError) {
                    mView.dismissLoading()
                    mView.showError(Error.dialog(error.message))
                }

                override fun onSuccess(result: UserContainer) {
                    val userManager = UserManager.instance
                    userManager.setUserContainer(mView.context, result)
                    mView.dismissLoading()
                    mView.showSuccess()
                }
            })
    }


    /**
     * 保存用户信息
     */
    private fun saveUserInfo(account: String) {
        StorageUtils.with(mView.context)
            .param(UserContainer.Key.ACCOUNT, account)
            .save()
    }

    /**
     * 清除用户缓存
     */
    private fun clear() {
        StorageUtils.with(mView.context)
            .key(UserContainer.Key.CACHE)
            .remove()
    }
}
