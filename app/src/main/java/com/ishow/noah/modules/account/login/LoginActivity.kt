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

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.getInteger
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.utils.watcher.EnableTextWatcher
import com.ishow.common.utils.watcher.checker.PasswordChecker
import com.ishow.common.utils.watcher.checker.PhoneNumberChecker
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivityLoginBinding
import com.ishow.noah.modules.account.password.forgot.ForgotPasswordActivity
import com.ishow.noah.modules.account.register.RegisterActivity
import com.ishow.noah.modules.base.AppBindActivity
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by yuhaiyang on 2018/8/8.
 * 登录界面
 */
class LoginActivity : AppBindActivity<ActivityLoginBinding>() {


    private lateinit var mLoginViewModel: LoginViewModel
    private var mEnableWatcher = EnableTextWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_login)
        getViewModel(LoginViewModel::class.java).also {
            mBindingView.vm = it
            mLoginViewModel = it
            it.init()
        }
    }


    override fun initViews() {
        super.initViews()
        mEnableWatcher.setEnableView(login)
                .addChecker(account, PhoneNumberChecker())
                .addChecker(password, PasswordChecker(getInteger(R.integer.min_password)))
    }

    fun onViewClick(v: View) {
        when (v.id) {

            R.id.login -> {
                mLoginViewModel.login(account.inputText, password.inputText)
            }

            R.id.register -> {
                AppRouter.with(this)
                        .target(RegisterActivity::class.java)
                        .start()
            }

            R.id.forgotPassword -> {
                AppRouter.with(this)
                        .target(ForgotPasswordActivity::class.java)
                        .start()
            }
        }
    }


}
