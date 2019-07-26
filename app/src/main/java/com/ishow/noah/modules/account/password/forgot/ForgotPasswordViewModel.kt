package com.ishow.noah.modules.account.password.forgot

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.modules.binding.Event
import com.ishow.common.utils.StorageUtils
import com.ishow.noah.R
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.ForgotPasswordParams
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.base.mvvm.AppBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ForgotPasswordViewModel(application: Application) : AppBaseViewModel(application) {

    private val _verifyCodeStatus = MutableLiveData<Event<Boolean>>()
    val verifyCodeStatus: LiveData<Event<Boolean>>
        get() = _verifyCodeStatus

    private val _resetState = MutableLiveData<Event<Boolean>>()
    val resetState: LiveData<Event<Boolean>>
        get() = _resetState

    fun sendVerifyCode(phone: String) = GlobalScope.launch(Dispatchers.Main) {
        delay(2000)
        val result = Random().nextInt() % 2 == 0
        _verifyCodeStatus.value = Event(result)
        if (!result) toast("发送验证码失败")
    }

    fun resetPassword(phone: String, verifyCode: String, password: String, ensurePassword: String) {
        if (!TextUtils.equals(password, ensurePassword)) {
            toast(R.string.please_input_right_ensure_password)
            return
        }
        val params = ForgotPasswordParams()
        params.phone = phone
        params.code = verifyCode
        params.password = password

        GlobalScope.launch {
            val accountModel = AccountModel()
            val result: AppHttpResponse<Any> = withLoading { accountModel.forgotPassword(params) }
            if (result.isSuccess()) {
                mainThread { _resetState.value = Event(true) }
                StorageUtils.with(context)
                        .param(UserContainer.Key.ACCOUNT, phone)
                        .save()
            } else {
                toast(result.message)
            }
        }

    }
}