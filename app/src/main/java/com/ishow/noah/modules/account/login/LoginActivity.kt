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
import androidx.lifecycle.Observer
import com.ishow.common.extensions.open
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.utils.watcher.EnableTextWatcher
import com.ishow.common.utils.watcher.checker.PhoneNumberChecker
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivityLoginBinding
import com.ishow.noah.modules.account.password.forgot.ForgotPasswordActivity
import com.ishow.noah.modules.account.register.RegisterActivity
import com.ishow.noah.modules.base.mvvm.AppBindActivity
import com.ishow.noah.modules.main.MainActivity
import com.ishow.noah.utils.checker.PasswordChecker
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by yuhaiyang on 2018/8/8.
 * 登录界面
 */
class LoginActivity : AppBindActivity<ActivityLoginBinding>() {

    private lateinit var mLoginViewModel: LoginViewModel
    private var mEnableWatcher = EnableTextWatcher()
    private var mType = TYPE_FINISHSELF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_login)
        bindViewModel(LoginViewModel::class.java) {
            dataBinding.vm = it
            mLoginViewModel = it
            it.init()
        }
    }

    override fun initViews() {
        super.initViews()
        mEnableWatcher.setEnableView(login)
            .addChecker(account, PhoneNumberChecker())
            .addChecker(password, PasswordChecker(context))
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        mType = intent.getIntExtra(KEY_TYPE, TYPE_FINISHSELF)
    }

    fun onViewClick(v: View) {
        when (v.id) {

            R.id.login -> {
                mLoginViewModel.login(account.inputText, password.inputText)
            }

            R.id.register -> {
                open(RegisterActivity::class.java)
            }

            R.id.forgotPassword -> {
                open(ForgotPasswordActivity::class.java)
            }
        }
    }

    override fun showSuccess() {
        super.showSuccess()
        onBackPressed()
    }

    override fun onBackPressed() {
        if (mType == TYPE_FINISHSELF) {
            finish()
        } else {
            open(MainActivity::class.java, true)
        }
    }

    companion object {
        /**
         * 跳转首页
         */
        const val TYPE_GO_TOMAIN = 1
        /**
         * 回退
         */
        const val TYPE_FINISHSELF = 2
    }
}
