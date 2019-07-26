/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.account.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.extensions.getInteger
import com.ishow.common.extensions.openBrowser
import com.ishow.common.extensions.toast
import com.ishow.common.modules.binding.Event
import com.ishow.common.utils.IntentUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.utils.watcher.EnableTextWatcher
import com.ishow.common.utils.watcher.VerifyCodeTextWatcher
import com.ishow.common.utils.watcher.checker.PasswordChecker
import com.ishow.common.utils.watcher.checker.PhoneNumberChecker
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivityRegisterBinding
import com.ishow.noah.modules.base.AppBindActivity
import com.ishow.noah.modules.main.MainActivity
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Created by yuhaiyang on 2018/8/8.
 * 注册界面
 */
class RegisterActivity : AppBindActivity<ActivityRegisterBinding>() {

    private lateinit var mVerifyCodeWatcher: VerifyCodeTextWatcher
    private lateinit var mSubmitWatcher: EnableTextWatcher

    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_register)
        getViewModel(RegisterViewModel::class.java).also {
            observeLiveData(it)
            mViewModel = it
            mBindingView.vm = it
        }
    }

    override fun initViews() {
        super.initViews()

        mVerifyCodeWatcher = VerifyCodeTextWatcher()
                .setEnableView(sendVerifyCode)
                .addChecker(phone, PhoneNumberChecker())

        mSubmitWatcher = EnableTextWatcher()
                .setEnableView(submit)
                .addChecker(phone, PhoneNumberChecker())
                .addChecker(verifyCode)
                .addChecker(password, PasswordChecker(getInteger(R.integer.min_password)))
                .addChecker(ensurePassword, PasswordChecker(getInteger(R.integer.min_password)))
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.sendVerifyCode -> {
                sendVerifyCode.showLoading()
                mViewModel.sendVerifyCode(phone.inputText)
            }
            R.id.submit -> {
                mViewModel.register(phone.inputText, verifyCode.inputText, password.inputText, ensurePassword.inputText)
            }

            R.id.agreement -> {
                openBrowser("https://www.baidu.com/")
            }
        }
    }


    private fun observeLiveData(vm: RegisterViewModel) = vm.run {
        verifyCodeStatus.observe(this@RegisterActivity, Observer { onVerifyCodeStatusChanged(it) })
        registerState.observe(this@RegisterActivity, Observer { registerSuccess() })
    }

    private fun onVerifyCodeStatusChanged(status: Event<Boolean>) {
        status.getContent()?.let { success ->
            if (success) {
                sendVerifyCode.startTiming()
            } else {
                sendVerifyCode.reset()
            }
        }
    }

    private fun registerSuccess() {
        toast(R.string.register_success)
        AppRouter.with(context)
                .target(MainActivity::class.java)
                .flag(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .finishSelf()
                .start()
    }
}
