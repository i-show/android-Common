/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.account.register

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.ishow.common.entries.status.Error
import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.R
import com.ishow.noah.constant.Url
import com.ishow.noah.manager.UserManager
import com.ishow.noah.utils.http.AppHttpCallBack
import java.util.*

/**
 * Created by yuhaiyang on 2018/8/8.
 * 注册的Presenter
 */
internal class RegisterPresenter(private val mView: RegisterContract.View) : RegisterContract.Presenter {


    //  用来模拟网络连接的Handler
    private val mHandler = Handler()


    override fun register(context: Context, name: String, verifyCode: String, password: String, ensurePassword: String) {
        val errorMessage = UserManager.checkEnsurePassword(context, password, ensurePassword)
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(Error.dialog(errorMessage))
            return
        }

        mView.showLoading()

        val params = JSONObject()
        params["verifyCode"] = verifyCode
        params["phone"] = name
        params["password"] = password

        Http.post()
                .url(Url.register())
                .params(params.toString())
                .execute(object : AppHttpCallBack<String>(context) {
                    override fun onFailed(error: HttpError) {
                        mView.dismissLoading()
                        mView.showError(Error.dialog(error.message))
                    }

                    override fun onSuccess(result: String) {
                        mView.dismissLoading()
                        mView.showSuccess()
                    }
                })
    }

    override fun sendVerifyCode(context: Context, phoneNumber: String) {
        // 模拟发送验证码
        mHandler.postDelayed({
            val result = Random().nextInt()
            if (result % 2 == 0) {
                mView.showSendVerifySuccess()
            } else {
                mView.showSendVerifyFail("请求超时")
            }
        }, 3000)
    }

}
