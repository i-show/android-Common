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

package com.ishow.noah.modules.account.password.forgot

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import com.ishow.common.entries.status.Error

import com.ishow.noah.R
import com.ishow.noah.manager.UserManager

import java.util.Random

/**
 * Created by yuhaiyang on 2018/8/8.
 * 注册的Presenter
 */
internal class ForgotPasswordPresenter(private val mView: ForgotPasswordContract.View) : ForgotPasswordContract.Presenter {

    override fun resetPassword(context: Context, name: String, verifyCode: String, password: String, ensurePassword: String) {
        var errorMessage = UserManager.checkAccount(context, name)
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(Error.dialog(errorMessage))
            return
        }

        if (TextUtils.isEmpty(verifyCode)) {
            mView.showError(Error.dialog(R.string.register_please_input_verify_code))
            return
        }

        errorMessage = UserManager.checkPassword(context, password)
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(Error.dialog(errorMessage))
            return
        }

        errorMessage = UserManager.checkEnsurePassword(context, password, ensurePassword)
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(Error.dialog(errorMessage))
            return
        }

        mView.showLoading()


    }

    override fun sendVerifyCode(context: Context, phoneNumber: String) {

    }

}
